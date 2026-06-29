package kr.ac.kopo.member.service;

import java.util.List;

/**
 * 고유얼굴(Eigenfaces, PCA 기반 얼굴인식) 알고리즘을 외부 라이브러리 없이 직접 구현한 클래스.
 *
 * face-api.js 같은 사전학습된 신경망의 임베딩(descriptor)을 가져다 쓰는 게 아니라,
 * 등록된 얼굴들의 픽셀 벡터만으로 주성분분석(PCA)을 수행해 "고유얼굴" 기저를 직접
 * 계산하고, 그 공간에 사진을 투영(project)해서 거리를 비교한다 (Turk & Pentland, 1991).
 *
 * 표본 수(N)가 픽셀 차원(D)보다 훨씬 작을 때 D×D 공분산행렬 대신 N×N의 작은
 * 그램행렬(L = A^T A)을 고유분해하는 고전적인 트릭을 쓴다. N×N 대칭행렬의
 * 고유분해는 야코비(Jacobi) 회전법으로 직접 구현했다(외부 선형대수 라이브러리 미사용).
 */
public class EigenfaceRecognizer {

	/** 야코비 회전 최대 반복 횟수 — 대부분 수십 회 안에 충분히 수렴한다. */
	private static final int MAX_SWEEPS = 100;
	private static final double EPSILON = 1e-9;

	public static class Basis {
		public final double[] mean;
		public final double[][] eigenfaces; // [k][D], 각 행이 고유얼굴 1개

		public Basis(double[] mean, double[][] eigenfaces) {
			this.mean = mean;
			this.eigenfaces = eigenfaces;
		}
	}

	/**
	 * 학습 이미지(픽셀 벡터)들로부터 평균 얼굴과 상위 k개 고유얼굴을 계산한다.
	 * 표본이 2개 미만이면 PCA가 의미가 없으므로 null을 반환한다(호출 측에서 원본
	 * 픽셀 벡터의 유클리드 거리 비교로 대체해야 한다).
	 */
	public static Basis computeEigenfaces(List<double[]> images, int k) {
		int n = images.size();
		if (n < 2) {
			return null;
		}
		int d = images.get(0).length;

		double[] mean = new double[d];
		for (double[] img : images) {
			for (int j = 0; j < d; j++) {
				mean[j] += img[j];
			}
		}
		for (int j = 0; j < d; j++) {
			mean[j] /= n;
		}

		double[][] centered = new double[n][d];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < d; j++) {
				centered[i][j] = images.get(i)[j] - mean[j];
			}
		}

		// 그램행렬 L (N x N): L[i][j] = centered[i] · centered[j]
		double[][] gram = new double[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = i; j < n; j++) {
				double dot = 0;
				for (int x = 0; x < d; x++) {
					dot += centered[i][x] * centered[j][x];
				}
				gram[i][j] = dot;
				gram[j][i] = dot;
			}
		}

		EigenDecomposition eig = jacobiEigenDecomposition(gram);

		int kEff = Math.min(k, n - 1);
		double[][] eigenfaces = new double[kEff][d];
		for (int rank = 0; rank < kEff; rank++) {
			int col = eig.order[rank];
			double[] face = new double[d];
			for (int i = 0; i < n; i++) {
				double coeff = eig.eigenvectors[i][col];
				for (int j = 0; j < d; j++) {
					face[j] += coeff * centered[i][j];
				}
			}
			normalize(face);
			eigenfaces[rank] = face;
		}

		return new Basis(mean, eigenfaces);
	}

	/** 얼굴 픽셀 벡터를 고유얼굴 공간으로 투영해 k차원 가중치 벡터를 얻는다. */
	public static double[] project(double[] image, Basis basis) {
		int k = basis.eigenfaces.length;
		int d = image.length;
		double[] weights = new double[k];
		for (int i = 0; i < k; i++) {
			double sum = 0;
			for (int j = 0; j < d; j++) {
				sum += (image[j] - basis.mean[j]) * basis.eigenfaces[i][j];
			}
			weights[i] = sum;
		}
		return weights;
	}

	public static double euclideanDistance(double[] a, double[] b) {
		double sum = 0;
		for (int i = 0; i < a.length; i++) {
			double diff = a[i] - b[i];
			sum += diff * diff;
		}
		return Math.sqrt(sum);
	}

	private static void normalize(double[] v) {
		double norm = 0;
		for (double x : v) {
			norm += x * x;
		}
		norm = Math.sqrt(norm);
		if (norm < EPSILON) {
			return;
		}
		for (int i = 0; i < v.length; i++) {
			v[i] /= norm;
		}
	}

	private static class EigenDecomposition {
		double[] eigenvalues;
		double[][] eigenvectors; // 열(column) 단위로 고유벡터 저장
		int[] order; // 고유값 내림차순 인덱스
	}

	/** 대칭행렬 a(n x n)를 야코비 회전법으로 고유분해한다. a는 변경되지 않는다. */
	private static EigenDecomposition jacobiEigenDecomposition(double[][] a) {
		int n = a.length;
		double[][] m = new double[n][n];
		for (int i = 0; i < n; i++) {
			m[i] = a[i].clone();
		}
		double[][] v = new double[n][n];
		for (int i = 0; i < n; i++) {
			v[i][i] = 1.0;
		}

		for (int sweep = 0; sweep < MAX_SWEEPS; sweep++) {
			double off = 0;
			for (int i = 0; i < n; i++) {
				for (int j = i + 1; j < n; j++) {
					off += m[i][j] * m[i][j];
				}
			}
			if (off < EPSILON) {
				break;
			}

			for (int p = 0; p < n; p++) {
				for (int q = p + 1; q < n; q++) {
					if (Math.abs(m[p][q]) < EPSILON) {
						continue;
					}
					double theta = (m[q][q] - m[p][p]) / (2 * m[p][q]);
					double t = Math.signum(theta) / (Math.abs(theta) + Math.sqrt(theta * theta + 1));
					if (theta == 0) {
						t = 1;
					}
					double c = 1 / Math.sqrt(t * t + 1);
					double s = t * c;

					double mpp = m[p][p], mqq = m[q][q], mpq = m[p][q];
					m[p][p] = c * c * mpp - 2 * s * c * mpq + s * s * mqq;
					m[q][q] = s * s * mpp + 2 * s * c * mpq + c * c * mqq;
					m[p][q] = 0;
					m[q][p] = 0;

					for (int i = 0; i < n; i++) {
						if (i != p && i != q) {
							double mip = m[i][p], miq = m[i][q];
							m[i][p] = c * mip - s * miq;
							m[p][i] = m[i][p];
							m[i][q] = s * mip + c * miq;
							m[q][i] = m[i][q];
						}
					}
					for (int i = 0; i < n; i++) {
						double vip = v[i][p], viq = v[i][q];
						v[i][p] = c * vip - s * viq;
						v[i][q] = s * vip + c * viq;
					}
				}
			}
		}

		EigenDecomposition result = new EigenDecomposition();
		result.eigenvalues = new double[n];
		for (int i = 0; i < n; i++) {
			result.eigenvalues[i] = m[i][i];
		}
		result.eigenvectors = v;

		Integer[] idx = new Integer[n];
		for (int i = 0; i < n; i++) {
			idx[i] = i;
		}
		java.util.Arrays.sort(idx, (x, y) -> Double.compare(result.eigenvalues[y], result.eigenvalues[x]));
		result.order = new int[n];
		for (int i = 0; i < n; i++) {
			result.order[i] = idx[i];
		}
		return result;
	}
}

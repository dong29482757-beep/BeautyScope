package kr.ac.kopo.aspect.vo;

public class AspectVO {

	private String aspect;
	private int positiveCnt;
	private int neutralCnt;
	private int negativeCnt;
	private double positiveRatio;

	public String getAspect() {
		return aspect;
	}

	public void setAspect(String aspect) {
		this.aspect = aspect;
	}

	public int getPositiveCnt() {
		return positiveCnt;
	}

	public void setPositiveCnt(int positiveCnt) {
		this.positiveCnt = positiveCnt;
	}

	public int getNeutralCnt() {
		return neutralCnt;
	}

	public void setNeutralCnt(int neutralCnt) {
		this.neutralCnt = neutralCnt;
	}

	public int getNegativeCnt() {
		return negativeCnt;
	}

	public void setNegativeCnt(int negativeCnt) {
		this.negativeCnt = negativeCnt;
	}

	public double getPositiveRatio() {
		return positiveRatio;
	}

	public void setPositiveRatio(double positiveRatio) {
		this.positiveRatio = positiveRatio;
	}

	@Override
	public String toString() {
		return "AspectVO [aspect=" + aspect + ", positiveCnt=" + positiveCnt + ", neutralCnt=" + neutralCnt
				+ ", negativeCnt=" + negativeCnt + ", positiveRatio=" + positiveRatio + "]";
	}
}

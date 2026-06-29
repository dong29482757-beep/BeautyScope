<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>얼굴로 로그인 - BeautyScope</title>
<%@ include file="../common/header.jsp" %>
<script src="https://cdn.jsdelivr.net/npm/face-api.js@0.22.2/dist/face-api.min.js"></script>
</head>
<body>
	<%@ include file="../common/navbar.jsp" %>

	<section class="bs-section container" style="max-width:480px;">
		<h2>얼굴로 로그인</h2>
		<p style="color:var(--bs-ink-soft);font-size:13px;margin-bottom:18px;">
			얼굴 등록을 미리 해둔 경우에만 사용할 수 있어요.
			(마이페이지 &gt; 얼굴 로그인 등록에서 먼저 등록해주세요.)
		</p>

		<video id="video" width="400" height="300" autoplay muted style="border:1px solid var(--bs-line);background:#000;"></video>
		<p id="status" style="margin-top:12px;font-size:13px;color:var(--bs-ink-soft);">모델을 불러오는 중...</p>
		<button id="captureBtn" class="bs-btn" style="margin-top:10px;" disabled>얼굴로 로그인</button>

		<script>
			const MODEL_URL = 'https://cdn.jsdelivr.net/gh/justadudewhohacks/face-api.js@master/weights';
			const video = document.getElementById('video');
			const statusEl = document.getElementById('status');
			const captureBtn = document.getElementById('captureBtn');

			// face-api.js는 "얼굴이 어디 있는지"만 찾는 용도로만 쓴다(TinyFaceDetector).
			// "이 얼굴이 누구인지" 비교하는 알고리즘(고유얼굴/Eigenfaces, PCA)은
			// 외부 모델 없이 서버(EigenfaceRecognizer.java)에서 직접 구현했다.
			const FACE_SIZE = 48;

			function extractFaceVector(video, box) {
				const canvas = document.createElement('canvas');
				canvas.width = FACE_SIZE;
				canvas.height = FACE_SIZE;
				const ctx = canvas.getContext('2d');
				ctx.drawImage(video, box.x, box.y, box.width, box.height, 0, 0, FACE_SIZE, FACE_SIZE);
				const imgData = ctx.getImageData(0, 0, FACE_SIZE, FACE_SIZE).data;
				const vector = [];
				for (let i = 0; i < imgData.length; i += 4) {
					vector.push(Math.round(0.299 * imgData[i] + 0.587 * imgData[i + 1] + 0.114 * imgData[i + 2]));
				}
				return vector;
			}

			async function init() {
				await faceapi.nets.tinyFaceDetector.loadFromUri(MODEL_URL);

				const stream = await navigator.mediaDevices.getUserMedia({ video: {} });
				video.srcObject = stream;

				statusEl.textContent = '준비됐어요. 얼굴이 화면에 보이면 버튼을 누르세요.';
				captureBtn.disabled = false;
			}

			captureBtn.addEventListener('click', async () => {
				statusEl.textContent = '얼굴을 찾는 중...';
				const detection = await faceapi.detectSingleFace(video, new faceapi.TinyFaceDetectorOptions());

				if (!detection) {
					statusEl.textContent = '얼굴을 찾지 못했어요. 다시 시도해주세요.';
					return;
				}

				const pixels = extractFaceVector(video, detection.box);
				const res = await fetch('${ pageContext.request.contextPath }/login/face', {
					method: 'POST',
					headers: { 'Content-Type': 'application/json' },
					body: JSON.stringify(pixels)
				});
				const data = await res.json();
				if (data.success) {
					statusEl.textContent = '로그인 성공! 이동 중...';
					location.href = '${ pageContext.request.contextPath }/?msg=' + encodeURIComponent('얼굴 인식으로 로그인이 완료되었습니다.');
				} else {
					statusEl.textContent = '로그인 실패: ' + data.message;
				}
			});

			init().catch(err => {
				statusEl.textContent = '카메라를 불러오지 못했어요: ' + err.message;
			});
		</script>
	</section>

	<%@ include file="../common/footer.jsp" %>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>얼굴 로그인 등록 - BeautyScope</title>
<%@ include file="../common/header.jsp" %>
<script src="https://cdn.jsdelivr.net/npm/face-api.js@0.22.2/dist/face-api.min.js"></script>
</head>
<body>
	<%@ include file="../common/navbar.jsp" %>

	<section class="bs-section container" style="max-width:480px;">
		<h2>얼굴 로그인 등록</h2>
		<p style="color:var(--bs-ink-soft);font-size:13px;margin-bottom:18px;">
			웹캠으로 얼굴을 한 번 등록하면, 다음부터 비밀번호 없이 얼굴로 로그인할 수 있어요.
			(브라우저에서만 처리되는 데모 기능이라 보안 등급의 인증은 아니에요.)
		</p>

		<video id="video" width="400" height="300" autoplay muted style="border:1px solid var(--bs-line);background:#000;"></video>
		<p id="status" style="margin-top:12px;font-size:13px;color:var(--bs-ink-soft);">모델을 불러오는 중...</p>
		<button id="captureBtn" class="bs-btn" style="margin-top:10px;" disabled>얼굴 등록하기</button>

		<script>
			const MODEL_URL = 'https://cdn.jsdelivr.net/gh/justadudewhohacks/face-api.js@master/weights';
			const video = document.getElementById('video');
			const statusEl = document.getElementById('status');
			const captureBtn = document.getElementById('captureBtn');

			async function init() {
				await faceapi.nets.tinyFaceDetector.loadFromUri(MODEL_URL);
				await faceapi.nets.faceLandmark68Net.loadFromUri(MODEL_URL);
				await faceapi.nets.faceRecognitionNet.loadFromUri(MODEL_URL);

				const stream = await navigator.mediaDevices.getUserMedia({ video: {} });
				video.srcObject = stream;

				statusEl.textContent = '준비됐어요. 얼굴이 화면에 보이면 버튼을 누르세요.';
				captureBtn.disabled = false;
			}

			captureBtn.addEventListener('click', async () => {
				statusEl.textContent = '얼굴을 분석하는 중...';
				const detection = await faceapi.detectSingleFace(video, new faceapi.TinyFaceDetectorOptions())
					.withFaceLandmarks().withFaceDescriptor();

				if (!detection) {
					statusEl.textContent = '얼굴을 찾지 못했어요. 다시 시도해주세요.';
					return;
				}

				const descriptor = Array.from(detection.descriptor);
				const res = await fetch('${ pageContext.request.contextPath }/face/register', {
					method: 'POST',
					headers: { 'Content-Type': 'application/json' },
					body: JSON.stringify(descriptor)
				});
				const data = await res.json();
				statusEl.textContent = data.success ? '등록 완료! 다음부터 얼굴로 로그인할 수 있어요.' : '등록에 실패했어요: ' + data.message;
			});

			init().catch(err => {
				statusEl.textContent = '카메라를 불러오지 못했어요: ' + err.message;
			});
		</script>
	</section>

	<%@ include file="../common/footer.jsp" %>
</body>
</html>

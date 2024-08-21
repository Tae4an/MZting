from flask import request, jsonify
from . import app, bucket
import fal_client
from io import BytesIO
from PIL import Image
import base64
import uuid

@app.route('/gnimage', methods=['POST'])
def genimage():
    # 요청에서 JSON 데이터를 가져옵니다
    data = request.json
    
    # 필요한 프로퍼티들을 추출합니다
    prompt = data.get('prompt')
    
    # 프로퍼티들이 모두 제공되었는지 확인합니다
    if not all([prompt]):
        return jsonify({"error": "Missing required properties"}), 400
    
    def generate_and_upload_image(prompt):
        default_prompt = "photorealistic, high resolution, natural lighting, 20s beautiful Korean Girl, fair skin, light tan, family-friendly, high-resolution background, realistic details" #기본 프롬프트 설정
        combined_prompt = default_prompt + prompt

        handler = fal_client.submit(
            "fal-ai/flux",
            arguments={
                "prompt": combined_prompt,
                "sync_mode": True,
                "image_size": {
                    "width": 512,
                    "height": 768
                },
                "num_images": 1,
                "guidance_scale": 3.5,
                "num_inference_steps": 30,
                "enable_safety_checker": True
            }
        )

        result = handler.get()
        print(result)
        image_data = result['images'][0]['url']
        if image_data.startswith('data:image'):
            header, image_data = image_data.split(',')
        image_data = base64.b64decode(image_data)
        
        # 이미지를 메모리에 저장
        image = Image.open(BytesIO(image_data))
        
        # 랜덤한 파일 이름 생성
        file_name = f"{uuid.uuid4()}.png"
        
        # 이미지를 Firebase Storage에 업로드
        blob = bucket.blob(file_name)
        blob.upload_from_string(image_data, content_type='image/png')
        
        # 업로드된 이미지의 공개 URL 가져오기
        blob.make_public()
        public_url = blob.public_url
        
        return public_url
    
    imageUrl = generate_and_upload_image(prompt=prompt)
    
    return jsonify({
        "imageUrl" : imageUrl
    }), 200
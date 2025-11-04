import json
from flask import Flask, request, jsonify
from flask_cors import CORS
from PIL import Image
import easyocr
import openai
import os
from dotenv import load_dotenv

load_dotenv()
app = Flask(__name__)
CORS(app)

ocr_reader = easyocr.Reader(['en', 'hu'])
openai.api_key = os.environ.get("OPENAI_API_KEY")

PROMPT_TEMPLATE = """
Az alábbi szöveg egy könyv borítójáról származik. Kérlek, azonosítsd, hogy pontosan egy könyv szerepel-e rajta, majd töltsd ki az alábbi JSON mezőket: {{ "author": "", "description": "", "title": "" }} A "description" legyen 3-4 mondat hosszú és magyar nyelvű! Ha nem pontosan egy könyv szerepel a szöveg alapján, akkor válaszd ezt: {{"error": "A képen nem pontosan egy könyv található."}}

Szöveg:
{}
"""

@app.route("/api/books/extract", methods=["POST"])
def extract_book_data():
    if 'file' not in request.files:
        return jsonify({"error": "No file provided"}), 400

    file = request.files['file']
    image = Image.open(file.stream)

    ocr_results = ocr_reader.readtext(image)
    ocr_text = "\n".join([res[1] for res in ocr_results])
    prompt = PROMPT_TEMPLATE.format(ocr_text)

    try:
        completion = openai.chat.completions.create(
            model="gpt-4.1-nano",
            messages=[
                {"role": "system", "content": "Magyar könyvinformáció szövegfeldolgozó vagy."},
                {"role": "user", "content": prompt}
            ],
            max_tokens=400,
            temperature=0.2,
        )
        answer = completion.choices[0].message.content

        # openai_response egy string formájú JSON, parse-olni kell
        try:
            parsed = json.loads(answer)
        except Exception as e:
            return jsonify({"error": "AI válasza nem volt JSON formátumú.", "details": answer}), 500

        # Ha error mező van, azt adjuk vissza
        if "error" in parsed:
            return jsonify({"error": parsed["error"]}), 200

        # Egyébként a 3 mező: title, author, description (hiányzó mezőkre üres string)
        return jsonify({
            "title": parsed.get("title", ""),
            "author": parsed.get("author", ""),
            "description": parsed.get("description", ""),
        }), 200

    except Exception as e:
        return jsonify({"error": "OpenAI API hiba", "details": str(e)}), 500

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)
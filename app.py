from flask import Flask, request, jsonify
import pickle
import numpy as np

model = pickle.load(open('model.pkl', 'rb'))

app = Flask(__name__)


@app.route('/')
def home():
    return "Hello World"


@app.route('/predict', methods=['POST'])
def predict():
    cgpa = request.form.get('cgpa')
    iq = request.form.get('iq')
    profile_score = request.form.get('profile_score')

    input_query = np.array([[cgpa, iq, profile_score]])

    result = model.predict(input_query)[0]

    return jsonify({'placement': str(result)})


if __name__ == '__main__':
    app.run(debug=True)

# hello hi are you

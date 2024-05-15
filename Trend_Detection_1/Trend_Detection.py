import csv
from datetime import datetime
import nltk
from nltk.tokenize import word_tokenize
from nltk.probability import FreqDist
from nltk.corpus import stopwords
from nltk.stem import PorterStemmer
import pandas as pd
import matplotlib.pyplot as plt

nltk.download('punkt')
nltk.download('stopwords')

stop_words = set(stopwords.words('english'))
ps = PorterStemmer()

with open('output_2.csv', 'r', encoding='latin1') as file:
    text = file.read()

words = word_tokenize(text.lower())

filtered_words = [ps.stem(word) for word in words if word.isalnum() and word not in stop_words]

fdist = FreqDist(filtered_words)

top_10_words = fdist.most_common(10)

df = pd.DataFrame(top_10_words, columns=['Word', 'Frequency'])

print(df)

plt.figure(figsize=(10, 6))
plt.axis('off')
table = plt.table(cellText=df.values, colLabels=df.columns, loc='center', cellLoc='center')
table.auto_set_font_size(False)
table.set_fontsize(12)
plt.savefig('frequency_table.png', bbox_inches='tight', pad_inches=0.1)


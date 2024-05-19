import csv
from datetime import datetime, timedelta
import nltk
from nltk.tokenize import word_tokenize
from nltk.corpus import stopwords
from nltk.stem import PorterStemmer
import matplotlib.pyplot as plt
import matplotlib.dates as mdates

stop_words = set(stopwords.words('english'))
ps = PorterStemmer()

search_word = input("Enter the word you want to search for: ").lower()
stemmed_search_word = ps.stem(search_word)

data = []
with open('Output_test.csv', 'r', encoding='ISO-8859-1') as file:
    csv_reader = csv.reader(file)
    for row in csv_reader:
        if len(row) == 3:  
            url, date_str, content = row
            data.append((date_str, content))

start_date = datetime.strptime("01/01/2022", '%d/%m/%Y')
end_date = datetime.strptime("01/01/2023", '%d/%m/%Y')

word_counts = []

for date_str, content in data:
    if date_str:
        date_parts = date_str.split('/')
        if len(date_parts) == 3 and len(date_parts[2]) == 2:
            date_parts[2] = '20' + date_parts[2]
            date_str = '/'.join(date_parts)

        try:
            article_date = datetime.strptime(date_str, '%d/%m/%Y')
            if start_date <= article_date <= end_date:
                words = word_tokenize(content.lower())
                filtered_words = [ps.stem(word) for word in words if word.isalnum() and word not in stop_words]
                word_count = filtered_words.count(stemmed_search_word)
                word_counts.append((article_date, word_count))
        except ValueError:
            pass

word_counts.sort(key=lambda x: x[0])

dates = [wc[0] for wc in word_counts]
counts = [wc[1] for wc in word_counts]

plt.plot(dates, counts, marker='o')
plt.xlabel('Date')
plt.ylabel(f'Frequency of "{search_word}"')
plt.title(f'Frequency of "{search_word}" over Time')
plt.xticks(rotation=45)

# Calculate the number of ticks you want on the x-axis
num_ticks = 10
days_interval = (end_date - start_date) / num_ticks

# Set the tick frequency to show 10 ticks on the x-axis
plt.gca().xaxis.set_major_locator(mdates.DayLocator(interval=days_interval.days))

plt.grid(True)
plt.tight_layout()

plt.savefig('frequency_word.png')

plt.show()

max_count = max(counts)
scores = [count / max_count * 50 for count in counts]
print("Scores:", scores)

from string import ascii_uppercase

if __name__ == '__main__':
    dic = dict()
    for letter in ascii_uppercase:
        dic[letter] = 0
    with open('names.txt', 'r') as names:
        for name in names:
            letter = name[0].upper()
            dic[letter] += 1
    with open('names_counting.txt', 'w') as names_counting:
        for letter, count in dic.items():
            names_counting.write(f'SET {letter} {count}\n')
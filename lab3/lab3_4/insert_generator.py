followers = {
    'z_praxz': ['ViniciusLucius', 'diomont01'],
    'ViniciusLucius': ['z_praxz', 'quietglitch', 'rospuye'],
    'quietglitch': ['z_praxz', 'ViniciusLucius', 'diomont01'],
    'diomont01': ['z_praxz', 'quietglitch', 'rospuye'],
    'rospuye': ['ViniciusLucius', 'quietglitch', 'diomont01'],
}

following = {
    'z_praxz': ['ViniciusLucius', 'diomont01', 'quietglitch'],
    'ViniciusLucius': ['z_praxz', 'quietglitch', 'rospuye'],
    'quietglitch': ['diomont01', 'ViniciusLucius', 'rospuye'],
    'diomont01': ['quietglitch', 'rospuye', 'z_praxz'],
    'rospuye': ['diomont01', 'ViniciusLucius']
}

def dweed(id, poster, content, date):
    string = f"""insert into dweeds(id, poster, content, date)
values({id}, '{poster}', '{content}', '{date}');

insert into user_dweeds(user_handle, dweed_id, dweed_date)
values('{poster}', {id}, '{date}');

"""

    for user in followers.get(poster):
        string += f"""insert into feeds(user_handle, dweed_id, dweed_date)
values('{user}', {id}, '{date}');

"""
    
    return string

def main():
    dweeds = [
        # dweed(1, 'ViniciusLucius', 'com o miranha lancado tenho medo de usar o twitter ent n me julguem se eu sumir', '2021-12-14 19:52:26'),
        # dweed(2, 'z_praxz', 'ngl, cassandra esta a ser muito penoso', '2021-12-16 16:49:07'),
        # dweed(3, 'quietglitch', 'STEAM GAMES FINALLY COME UP IN THE START MENU BLESS WHOEVER DID THIS', '2021-12-12 15:46:58'),
        # dweed(4, 'diomont01', 'Just inhaled a lethal dose of carbon monoxide to better understand what goes on inside the heads of my brain-dead opponents.', '2021-11-22 17:58:02'),
        # dweed(5, 'rospuye', 'vou ter que ver o @ViniciusLucius dnv', '2021-12-12 18:35:26')
        dweed(6, 'quietglitch', 'Today I found out that fried pickles are a thing, now must buy pickles.', '2021-12-18 12:30:03'),
        dweed(7, 'quietglitch', 'a camila n fez este tweet mas quero mais', '2021-12-19 16:57:59')
    ]
    for d in dweeds:
        print(d)
if __name__ == '__main__':
    main()
import random
import sys

REALMS = {
    "The MacMillan Estate": [
        "Coal Tower",
        "Groaning Storehouse",
        "Ironworks of Misery",
        "Shelter Woods",
        "Suffocation Pit"
    ],
    "Crotus Prenn Asylum": [
        "Disturbed Ward",
        "Father Campbell's Chapel"
    ],
    "Haddonfield": [
        "Lampkin Lane"
    ],
    "Backwater Swamp": [
        "The Pale Rose",
        "Grim Pantry"
    ]
}

POSSIBLE_KILLS = [0, 1, 2, 3, 4]

POSSIBLE_HOOKS = [
    [0, 8],
    [3, 9],
    [6, 10],
    [9, 11],
    [12, 12]
]

ED = "Entity Displeased"
BK = "Brutal Killer"
RK = "Ruthless Killer"
MK = "Merciless Killer"

POSSIBLE_RESULTS = [
    [ED],
    [ED, BK],
    [ED, BK],
    [RK],
    [RK, MK]
]

KILLERS = [
    "Trapper",
    "Wraith",
    "Hillbilly",
    "Nurse",
    "Shape",
    "Hag",
    "Doctor",
    "Huntress",
    "Cannibal",
    "Nightmare",
    "Pig",
    "Clown",
    "Spirit",
    "Legion",
    "Plague",
    "Ghost Face",
    "Demogorgon",
    "Oni",
    "Deathslinger",
    "Executioner",
    "Blight",
    "Twins",
    "Trickster",
    "Nemesis",
    "Cenobite",
    "Artist"
]

SURVIVORS = [
    "Dwight Fairfield",
    "Meg Thomas",
    "Claudette Morel",
    "Jake Park",
    "Nea Karlsson",
    "Laurie Strode",
    "Ace Visconti",
    "William \"Bill\" Overbeck",
    "Feng Min",
    "David King",
    "Quentin Smith",
    "Detective David Tapp",
    "Kate Denson",
    "Adam Francis",
    "Jeffrey \"Jeff\" Johansen",
    "Jane Romero",
    "Ash J. Williams",
    "Nancy Wheeler",
    "Steve Harrington",
    "Yui Kimura",
    "Zarina Kassir",
    "Cheryl Mason",
    "Felix Richter",
    "Élodie Rakoto",
    "Yun-Jin Lee",
    "Jill Valentine",
    "Leon Scott Kennedy",
    "Mikaela Reid",
    "Jonah Vasquez"
]

# KILLER_PERKS = [
#     "Barbecue & Chilli",
#     "Corrupt Intervention",
#     "Tinkerer",
#     "Pop Goes The Weasel",
#     "Discordance",
#     "Hex: Ruin",
#     "Sloppy Butcher",
#     "A Nurse's Calling",
#     "Save The Best For Last",
#     "Infectious Fright"
# ]
#
# SURVIVOR_PERKS = [
#     "Borrowed Time",
#     "Boon: Circle of Healing",
#     "Dead Hard",
#     "Iron Will",
#     "Spine Chill",
#     "Kindred",
#     "Decisive Strike",
#     "Boon: Shadow Step",
#     "We're Gonna Live Forever",
#     "Unbreakable"
# ]

PLAYERS = [
    "praxz",
    "Trivialiac",
    "TrajceBrukata",
    "Abadjar",
    "Otzdarva",
    "Ev3ntic",
    "Fungoose",
    "Uncharted",
    "ZubatLEL",
    "Ralph",
    "Yerv",
    "TheJRM_",
    "Ohmwrecker",
    "hexy",
    "TrU3Ta1ent",
    "OhTofu",
    "Ayrun",
    "Umbra",
    "SpookyLoopz",
    "ANGRYPUG",
    "Skermz",
    "imPROBZZ",
    "Bronx",
    "ScottJund"
]


# def generate_build(side):
#     return random.sample((KILLER_PERKS if side == "killer" else SURVIVOR_PERKS), 4)


def generate_bp():
    return random.randrange(5000, 32001)


def generate_match(match_id):
    realm = random.choice(list(REALMS.keys()))
    _map = random.choice(REALMS[realm])

    killer = random.choice(KILLERS)
    players = random.sample(PLAYERS, 5)
    killer_player = players[0]
    survivor_players = players[1:]
    # Survivor characters can be repeated
    survivor_characters = [random.choice(SURVIVORS) for _ in range(4)]

    # killer_build = generate_build("killer")
    killer_bp = generate_bp()

    # survivor_builds = [generate_build("survivor") for _ in range(4)]
    survivor_bp = generate_bp()

    kills = random.choice(POSSIBLE_KILLS)
    killer_result = random.choice(POSSIBLE_RESULTS[kills])
    killer_hooks = random.choice(POSSIBLE_HOOKS[kills])

    # Determine which survivors escape
    escapes = [False] * kills + [True] * (4 - kills)
    random.shuffle(escapes)

    # Length of the match (minutes)
    length = random.randrange(7, 21)

    csv_line = f"{match_id},{length},{realm},{_map},{killer_player},{killer},{kills},{killer_hooks},{killer_result},{killer_bp}"
    for i in range(4):
        csv_line += f",{survivor_players[i]},{survivor_characters[i]},{escapes[i]},{survivor_bp}"
    return f"{csv_line}\n"


def main():
    header = "match_id,length,realm,map,killer_player,killer,kills,hooks,killer_result,killer_bp"
    for i in range(4):
        header += f",survivor{i}_player,survivor{i},survivor{i}_escaped,survivor{i}_bp"
    header += "\n"
    with open("matches.csv", "w") as f:
        n_matches = 1000 if len(sys.argv) < 2 else sys.argv[1]
        f.write(header)
        for match_id in range(n_matches):
            f.write(generate_match(match_id))


if __name__ == "__main__":
    main()

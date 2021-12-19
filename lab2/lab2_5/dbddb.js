const realms = {
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

const realmNames = [
    "The MacMillan Estate",
    "Crotus Prenn Asylum",
    "Haddonfield",
    "Backwater Swamp"
]

const possibleKills = [0, 1, 2, 3, 4]

const possibleHooks = [
    [0, 8],
    [3, 9],
    [6, 10],
    [9, 11],
    [12, 12]
]

const ed = "Entity Displeased";
const bk = "Brutal Killer";
const rk = "Ruthless Killer";
const mk = "Merciless Killer";

const possibleResults = [
    [ed],
    [ed, bk],
    [ed, bk],
    [rk],
    [rk, mk]
]

const killers = [
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

const killerPerks = [
    "Barbecue & Chilli",
    "Corrupt Intervention",
    "Tinkerer",
    "Pop Goes The Weasel",
    "Discordance",
    "Hex: Ruin",
    "Sloppy Butcher",
    "A Nurse's Calling",
    "Save The Best For Last",
    "Infectious Fright"
]

const survivorPerks = [
    "Borrowed Time",
    "Boon: Circle of Healing",
    "Dead Hard",
    "Iron Will",
    "Spine Chill",
    "Kindred",
    "Decisive Strike",
    "Boon: Shadow Step",
    "We're Gonna Live Forever",
    "Unbreakable"
]

const players = [
    "Otzdarva",
    "Ev3ntic",
    "Fungoose",
    "Uncharted",
    "ZubatLEL",
    "Ralph",
    "Yerv",
    "praxz",
    "Trivialiac",
]

// Random integer in [min, max]
function getRandomInt(min, max) {
    min = Math.ceil(min)
    max = Math.floor(max)
    return Math.floor(Math.random() * (max - min + 1)) + min
}

/**
 * Shuffles array in place.
 * @param {Array} a items An array containing the items.
 */
 function shuffle(a) {
    var j, x, i;
    for (i = a.length - 1; i > 0; i--) {
        j = Math.floor(Math.random() * (i + 1));
        x = a[i];
        a[i] = a[j];
        a[j] = x;
    }
    return a;
}

function randomGrade() {
    return getRandomInt(1, 20)
}

function generateBuild(side) {
    // return Math.floor(Math.random() * perks.length)
    perks = (side == "killer") ? [...killerPerks] : [...survivorPerks]
    shuffle(perks)
    build = []
    for (let i = 0; i < 4; i++)
        build.push(perks[i])
    return build
}

function generateBP() {
    bp = []
    for (let i = 0; i < 4; i++)
        bp.push(getRandomInt(2000, 8000))
    return bp
}

 function generateMatch() {
    let realm = realmNames[Math.floor(Math.random() * realmNames.length)]
    let realmMaps = realms[realm]
    let map = realmMaps[Math.floor(Math.random() * realmMaps.length)]

    let killer = killers[Math.floor(Math.random() * killers.length)]
    let playersCopy = [...players]
    
    // let killerPlayer = playersCopy.pop(Math.floor(Math.random() * playersCopy.length))
    shuffle(playersCopy)
    let killerPlayer = playersCopy[0]
    let survivorPlayers = []

    let killerBuild = generateBuild("killer")

    let killerBP = generateBP()
    
    let survivorBuilds = []
    let survivorGrades = []
    let survivorBP = []

    // for every survivor
    for (let i = 1; i < 5; i++) { 
        survivorPlayers.push(playersCopy[i]) // add player names to list
        let build = generateBuild("survivor")
        survivorBuilds.push(build) // add build
        survivorGrades.push(randomGrade()) // add grade
        survivorBP.push(generateBP()) // add bloodpoints (score in multiple categories)
    }
        
    let kills = possibleKills[Math.floor(Math.random() * possibleKills.length)]
    let killerPossibleResults = possibleResults[kills]
    let killerResult = killerPossibleResults[Math.floor(Math.random() * killerPossibleResults.length)]
    let killerPossibleHooks = possibleHooks[kills]
    let hooks = killerPossibleHooks[Math.floor(Math.random() * killerPossibleHooks.length)]
    let killerGrade = randomGrade()

    // determine which survivors escape
    let escapes = [] 
    for (let i = 0; i < kills; i++)
        escapes.push(false)
    for (let i = 0; i < 4 - kills; i++)
        escapes.push(true)
    shuffle(escapes)


    db.dbd.insertOne({
        "mapinfo": {
            "realm": realm,
            "map": map
        },
        "killer": {
            "player": killerPlayer,
            "grade": killerGrade,
            "character": killer,
            "perks": killerBuild,
            "kills": kills,
            "hooks": hooks,
            "result": killerResult,
            "score": {
                "brutality": killerBP[0],
                "deviousness": killerBP[1],
                "hunter": killerBP[2],
                "sacrifice": killerBP[3],
            }
        },
        "survivors": [
            {
                "player": survivorPlayers[0],
                "grade": survivorGrades[0],
                "perks": survivorBuilds[0],
                "escaped": escapes[0],
                "score": {
                    "objectives": survivorBP[0][0],
                    "survival": survivorBP[0][1],
                    "altruism": survivorBP[0][2],
                    "boldness": survivorBP[0][3]
                }
            },
            {
                "player": survivorPlayers[1],
                "grade": survivorGrades[1],
                "perks": survivorBuilds[1],
                "escaped": escapes[1],
                "score": {
                    "objectives": survivorBP[1][0],
                    "survival": survivorBP[1][1],
                    "altruism": survivorBP[1][2],
                    "boldness": survivorBP[1][3]
                }
            },
            {
                "player": survivorPlayers[2],
                "grade": survivorGrades[2],
                "perks": survivorBuilds[2],
                "escaped": escapes[2],
                "score": {
                    "objectives": survivorBP[2][0],
                    "survival": survivorBP[2][1],
                    "altruism": survivorBP[2][2],
                    "boldness": survivorBP[2][3]
                }
            },
            {
                "player": survivorPlayers[3],
                "grade": survivorGrades[3],
                "perks": survivorBuilds[3],
                "escaped": escapes[3],
                "score": {
                    "objectives": survivorBP[3][0],
                    "survival": survivorBP[3][1],
                    "altruism": survivorBP[3][2],
                    "boldness": survivorBP[3][3]
                }
            },
        ]
    })
}

function generateMatches(num) {
    for (let i = 0; i < num; i++)
        generateMatch()
}
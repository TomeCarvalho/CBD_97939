prefixNumPhones = function() {
    print(
        db.phones.aggregate([
            {
                $group: {
                    "_id": "$components.prefix",
                    "num_phones": { $sum: 1 }
                }
            }
        ])
    )
}

// Checks if a phone number (with the format "{country_code}-{number}") is a palindrome
phoneNumIsPalindrome = function(display) {
    let str = display.split("-")[1]
    reverse = str.split("").reverse().join("")
    return str == reverse
}

// Shows all phone numbers that are palindromes
phoneNumPalindromes = function() {
    print(
        db.phones.aggregate(
            [
                {
                    $addFields: 
                    {
                        "palindrome": 
                        {
                            "$function": 
                            {
                                body: phoneNumIsPalindrome,
                                args: ["$display"],
                                lang: "js"
                            }
                        }
                    }
                },
                {
                    $match:
                    {
                        "palindrome": true
                    }
                }
            ]
        )
    )
}
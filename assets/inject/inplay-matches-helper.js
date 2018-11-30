function parseSportConfigurationFromAttrs(attrs) {
    let conf = {
        sportName: attrs.NA, //Tennis
        sportId: attrs.ID, //13
        wsId: attrs.IT, //OV_13_1_9
        marketTypes: [] //for handicap markets see HM
    }

    if (attrs.MR) {
        let markets = attrs.MR.split('^')
        for (let i = 1; i < markets.length; i++) {
            let mAttrs = markets[i].split('$')
            conf.marketTypes.push({
                id: mAttrs[0],
                name: mAttrs[1]
            })
        }
    }

    return conf
}

function ReadOddDataOnBasicMarket(sportName, marketName) {
    let sports = getSports()
    if (!sports) {
        console.log('could not parse sports')
        return
    }

    let sport = sports.find(sp => sp.sportName === sportName)
    if (!sport) {
        console.log('could not find sport for sportName: ' + sportName)
        return
    }

    let market = sport.marketTypes.find(mt => mt.name === marketName)
    if (!market) {
        console.log('could not find market for marketName:' + marketName + ' of ' + sportName)
        return
    }

    let matchedMarkets = lookupNameInTree(market.id, 'MA')
    return formatMatchesFromMatchedMarkets(sportName, matchedMarkets)
}

function formatMatchesFromMatchedMarkets(sportName, matchedMarkets) {
    let matches = []

    M: for (let matchedMarket of matchedMarkets) {
        let idParts = matchedMarket.data.IT.split('-')
        let events = lookupNameInTree(idParts[1], 'EV')
        events = events.filter(ev => ev._delegateList.length > 0)
        if (!events || !events[0]) {
            continue
        }

        let playerNames = events[0].data.NA.split(' v ')

        let marketStates = []

        if (!matchedMarket._actualChildren[0] || !matchedMarket._actualChildren[0]._actualChildren) {
            continue
        }

        let marketChildren
        if (matchedMarket._actualChildren[0] && matchedMarket._actualChildren[0].nodeName === 'PA') {
            marketChildren = matchedMarket._actualChildren
        } else if (matchedMarket._actualChildren[0] && matchedMarket._actualChildren[0].nodeName === 'CO') {
            marketChildren = matchedMarket._actualChildren[0]._actualChildren
        } else {
            continue
        }

        for (let i = 0; i < marketChildren.length; i++) {
            if (marketChildren[i].data.SU === '1') {
                marketChildren[i].data.OD = '999/1' //make sure to record but make it obvious the market was suspended at this time
            }

            marketStates.push(createMarketStateObject(playerNames[i], marketChildren[i].data, matchedMarket))
        }

		let matchState = createMatchStateObject(sportName, events[0].data, marketStates)
        let match = createMatchObject(sportName, events[0].data, matchState)
        matches.push(match)
    }

    return matches
}

function createMarketStateObject(playerName, data, matchedMarket) {
    return {
    	marketName: matchedMarket.data.NA,
        playerName: playerName,
        betId: +data.FI,
        fixtureId: +data.ID,
        suspended: data.SU,
        odd: data.OD
    }
}

function createMatchStateObject(sportName, data, marketStates) {
    return {
        pointScore: data.XP,
        servingIndex: data.PI,
        setScore: data.SS,
        marketStates
    }
}

function createMatchObject(sportName, data, matchState) {
    return {
        id: +data.ID,
        name: data.NA,
        sportName: sportName,
        leagueName: data.CT,
        matchState
    }
}

//list all sports and markets
function getSports() {
    let sports = []
    for (let key in Locator.treeLookup._table) {
        if (Locator.treeLookup._table[key].nodeName === 'CL') {
            sports.push(parseSportConfigurationFromAttrs(Locator.treeLookup._table[key].data))
        }
    }

    return sports
}

function lookupNameInTree(keySearch, nodeName) {
    let results = []

    for (let key in Locator.treeLookup._table) {
        if (key.includes(keySearch) && Locator.treeLookup._table[key].nodeName === nodeName) {
            results.push(Locator.treeLookup._table[key])
        }
    }

    return results
}
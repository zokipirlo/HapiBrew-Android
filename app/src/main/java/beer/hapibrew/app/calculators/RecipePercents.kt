package beer.hapibrew.app.calculators

import beer.hapibrew.beerxml2proto.proto.Style

fun calculateOgPercent(og:Double):Double {
    return maxOf(minOf(((og - 1.0) / (Style.DEFAULT_OG_MAX - 1.0)), 1.0), 0.0)
}

fun calculateFgPercent(fg:Double):Double {
    return maxOf(minOf(((fg - 1.0) / (Style.DEFAULT_FG_MAX - 1.0)), 1.0), 0.0)
}

fun calculateIbuPercent(ibu:Double):Double {
    return maxOf(minOf((ibu / Style.DEFAULT_IBU_MAX), 1.0), 0.0)
}

fun calculateSrmPercent(srm:Double):Double {
    return maxOf(minOf((srm / Style.DEFAULT_SRM_MAX), 1.0), 0.0)
}

fun calculateAbvPercent(abv:Double):Double {
    return maxOf(minOf((abv / Style.DEFAULT_ABV_MAX), 1.0), 0.0)
}

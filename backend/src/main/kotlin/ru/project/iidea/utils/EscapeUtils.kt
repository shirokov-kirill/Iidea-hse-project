package ru.project.iidea.utils

fun escapeForLike(x: String) =
    x.replace("\\", "\\\\")
        .replace("_", "\\_")
        .replace("%", "\\%")
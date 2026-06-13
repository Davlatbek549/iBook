package com.example.dz.app_components.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Bespoke "Ink & Paper" icon set — 24px grid, 1.7px ink stroke, round caps,
 * filled dots are the only solid shapes. Path source: design/explorations/icons-ink.js.
 * Tint via Icon(tint = ...).
 */
object InkIcons {

    private val INK = SolidColor(Color(0xFF211C16))

    private fun ImageVector.Builder.stroke(pathBuilder: PathBuilder.() -> Unit) {
        path(
            fill = null,
            stroke = INK,
            strokeLineWidth = 1.7f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
            pathBuilder = pathBuilder,
        )
    }

    private fun ImageVector.Builder.dot(cx: Float, cy: Float, r: Float) {
        path(fill = INK) { circle(cx, cy, r) }
    }

    private fun inkIcon(name: String, content: ImageVector.Builder.() -> Unit): ImageVector =
        ImageVector.Builder(
            name = name,
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply(content).build()

    val BookOpen: ImageVector by lazy {
        inkIcon("InkBookOpen") {
            stroke {
                // open book cover
                moveTo(12f, 6.3f)
                curveTo(10.1f, 4.9f, 7.5f, 4.5f, 4.4f, 5f)
                verticalLineTo(17.9f)
                curveTo(7.5f, 17.4f, 10.1f, 17.8f, 12f, 19.2f)
                curveTo(13.9f, 17.8f, 16.5f, 17.4f, 19.6f, 17.9f)
                verticalLineTo(5f)
                curveTo(16.5f, 4.5f, 13.9f, 4.9f, 12f, 6.3f)
                close()
                // spine
                moveTo(12f, 6.3f)
                verticalLineTo(19.2f)
                // left page line
                moveTo(7.3f, 8.6f)
                curveTo(8.6f, 8.4f, 9.7f, 8.6f, 10.6f, 9.2f)
                // right page line
                moveTo(16.7f, 8.6f)
                curveTo(15.4f, 8.4f, 14.3f, 8.6f, 13.4f, 9.2f)
            }
        }
    }

    val Plus: ImageVector by lazy {
        inkIcon("InkPlus") {
            stroke {
                moveTo(12f, 5.4f)
                verticalLineTo(18.6f)
                moveTo(5.4f, 12f)
                horizontalLineTo(18.6f)
            }
        }
    }

    val Back: ImageVector by lazy {
        inkIcon("InkBack") {
            stroke {
                moveTo(14.6f, 4.8f)
                lineTo(7.4f, 12f)
                lineTo(14.6f, 19.2f)
            }
        }
    }

    val Email: ImageVector by lazy {
        inkIcon("InkEmail") {
            stroke {
                moveTo(4.4f, 8.3f)
                curveTo(4.4f, 6.8f, 5.6f, 5.6f, 7.1f, 5.6f)
                horizontalLineTo(16.9f)
                curveTo(18.4f, 5.6f, 19.6f, 6.8f, 19.6f, 8.3f)
                verticalLineTo(15.7f)
                curveTo(19.6f, 17.2f, 18.4f, 18.4f, 16.9f, 18.4f)
                horizontalLineTo(7.1f)
                curveTo(5.6f, 18.4f, 4.4f, 17.2f, 4.4f, 15.7f)
                close()
                moveTo(5.6f, 7.4f)
                lineTo(12f, 12.3f)
                lineTo(18.4f, 7.4f)
            }
        }
    }

    val Lock: ImageVector by lazy {
        inkIcon("InkLock") {
            stroke {
                moveTo(6.2f, 13.2f)
                curveTo(6.2f, 12f, 7.2f, 11f, 8.4f, 11f)
                horizontalLineTo(15.6f)
                curveTo(16.8f, 11f, 17.8f, 12f, 17.8f, 13.2f)
                verticalLineTo(17.3f)
                curveTo(17.8f, 18.5f, 16.8f, 19.5f, 15.6f, 19.5f)
                horizontalLineTo(8.4f)
                curveTo(7.2f, 19.5f, 6.2f, 18.5f, 6.2f, 17.3f)
                close()
                moveTo(8.8f, 11f)
                verticalLineTo(8.3f)
                curveTo(8.8f, 6.4f, 10.2f, 5f, 12f, 5f)
                curveTo(13.8f, 5f, 15.2f, 6.4f, 15.2f, 8.3f)
                verticalLineTo(11f)
            }
            dot(12f, 15.2f, 1.2f)
        }
    }

    val User: ImageVector by lazy {
        inkIcon("InkUser") {
            stroke {
                circle(12f, 8.2f, 3.5f)
                moveTo(5.6f, 19.5f)
                curveTo(5.6f, 16f, 8.4f, 14f, 12f, 14f)
                curveTo(15.6f, 14f, 18.4f, 16f, 18.4f, 19.5f)
            }
        }
    }

    val At: ImageVector by lazy {
        inkIcon("InkAt") {
            stroke {
                circle(12f, 12f, 3.3f)
                moveTo(15.3f, 12f)
                verticalLineTo(13.3f)
                curveTo(15.3f, 14.9f, 16.2f, 15.7f, 17.3f, 15.7f)
                curveTo(18.8f, 15.7f, 19.5f, 14.4f, 19.5f, 12f)
                curveTo(19.5f, 7.7f, 16.2f, 4.5f, 12f, 4.5f)
                curveTo(7.9f, 4.5f, 4.5f, 7.9f, 4.5f, 12f)
                curveTo(4.5f, 16.1f, 7.9f, 19.5f, 12f, 19.5f)
                curveTo(13.7f, 19.5f, 15.1f, 19f, 16.3f, 18.2f)
            }
        }
    }

    val Eye: ImageVector by lazy {
        inkIcon("InkEye") {
            stroke {
                eyeOutline()
                circle(12f, 12f, 2.4f)
            }
        }
    }

    val EyeOff: ImageVector by lazy {
        inkIcon("InkEyeOff") {
            stroke {
                eyeOutline()
                circle(12f, 12f, 2.4f)
                moveTo(5.4f, 18.6f)
                lineTo(18.6f, 5.4f)
            }
        }
    }

    val Home: ImageVector by lazy {
        inkIcon("InkHome") {
            stroke {
                moveTo(4f, 10.8f)
                lineTo(12f, 4.2f)
                lineTo(20f, 10.8f)
                moveTo(6f, 9.6f)
                lineTo(6f, 19.5f)
                lineTo(18f, 19.5f)
                lineTo(18f, 9.6f)
                moveTo(9.9f, 19.5f)
                verticalLineTo(16f)
                curveTo(9.9f, 14.8f, 10.8f, 14f, 12f, 14f)
                curveTo(13.2f, 14f, 14.1f, 14.8f, 14.1f, 16f)
                verticalLineTo(19.5f)
            }
        }
    }

    val Search: ImageVector by lazy {
        inkIcon("InkSearch") {
            stroke {
                circle(10.6f, 10.6f, 6.1f)
                moveTo(15.1f, 15.1f)
                lineTo(19.6f, 19.6f)
            }
        }
    }

    val Book: ImageVector by lazy {
        inkIcon("InkBook") {
            stroke {
                moveTo(12f, 6.3f)
                curveTo(10.1f, 4.9f, 7.5f, 4.5f, 4.4f, 5f)
                verticalLineTo(17.9f)
                curveTo(7.5f, 17.4f, 10.1f, 17.8f, 12f, 19.2f)
                curveTo(13.9f, 17.8f, 16.5f, 17.4f, 19.6f, 17.9f)
                verticalLineTo(5f)
                curveTo(16.5f, 4.5f, 13.9f, 4.9f, 12f, 6.3f)
                close()
                moveTo(12f, 6.3f)
                verticalLineTo(19.2f)
            }
        }
    }

    val Shop: ImageVector by lazy {
        inkIcon("InkShop") {
            stroke {
                moveTo(6.3f, 8.6f)
                lineTo(5.4f, 19.5f)
                lineTo(18.6f, 19.5f)
                lineTo(17.7f, 8.6f)
                close()
                moveTo(9.2f, 8.6f)
                verticalLineTo(7.4f)
                curveTo(9.2f, 5.8f, 10.4f, 4.5f, 12f, 4.5f)
                curveTo(13.6f, 4.5f, 14.8f, 5.8f, 14.8f, 7.4f)
                verticalLineTo(8.6f)
            }
        }
    }

    val Close: ImageVector by lazy {
        inkIcon("InkClose") {
            stroke {
                moveTo(6.2f, 6.2f)
                lineTo(17.8f, 17.8f)
                moveTo(17.8f, 6.2f)
                lineTo(6.2f, 17.8f)
            }
        }
    }

    val Star: ImageVector by lazy {
        inkIcon("InkStar") {
            stroke {
                moveTo(12f, 4.3f)
                lineTo(14.3f, 9.1f)
                lineTo(19.6f, 9.8f)
                lineTo(15.7f, 13.5f)
                lineTo(16.7f, 18.8f)
                lineTo(12f, 16.2f)
                lineTo(7.3f, 18.8f)
                lineTo(8.3f, 13.5f)
                lineTo(4.4f, 9.8f)
                lineTo(9.7f, 9.1f)
                close()
            }
        }
    }

    val Bookmark: ImageVector by lazy {
        inkIcon("InkBookmark") {
            stroke {
                moveTo(7f, 5.6f)
                curveTo(7f, 4.9f, 7.5f, 4.4f, 8.2f, 4.4f)
                horizontalLineTo(15.8f)
                curveTo(16.5f, 4.4f, 17f, 4.9f, 17f, 5.6f)
                verticalLineTo(19.6f)
                lineTo(12f, 15.9f)
                lineTo(7f, 19.6f)
                close()
            }
        }
    }

    val Tag: ImageVector by lazy {
        inkIcon("InkTag") {
            stroke {
                moveTo(4.5f, 5.7f)
                curveTo(4.5f, 5f, 5f, 4.5f, 5.7f, 4.5f)
                horizontalLineTo(10.9f)
                curveTo(11.5f, 4.5f, 12.1f, 4.7f, 12.5f, 5.2f)
                lineTo(19f, 11.6f)
                curveTo(19.8f, 12.4f, 19.8f, 13.7f, 19f, 14.5f)
                lineTo(14.5f, 19f)
                curveTo(13.7f, 19.8f, 12.4f, 19.8f, 11.6f, 19f)
                lineTo(5.2f, 12.5f)
                curveTo(4.7f, 12.1f, 4.5f, 11.5f, 4.5f, 10.9f)
                close()
            }
            dot(8.7f, 8.7f, 1.25f)
        }
    }

    val Stats: ImageVector by lazy {
        inkIcon("InkStats") {
            stroke {
                moveTo(4.6f, 19.4f)
                horizontalLineTo(19.4f)
                moveTo(7.4f, 19.4f)
                verticalLineTo(13.8f)
                moveTo(12f, 19.4f)
                verticalLineTo(8.6f)
                moveTo(16.6f, 19.4f)
                verticalLineTo(11.6f)
            }
        }
    }

    val Grid: ImageVector by lazy {
        inkIcon("InkGrid") {
            stroke {
                square(5f, 5f, 5.8f)
                square(13.2f, 5f, 5.8f)
                square(5f, 13.2f, 5.8f)
                square(13.2f, 13.2f, 5.8f)
            }
        }
    }

    val Done: ImageVector by lazy {
        inkIcon("InkDone") {
            stroke {
                moveTo(5.2f, 12.6f)
                lineTo(9.9f, 17.3f)
                lineTo(18.8f, 6.7f)
            }
        }
    }

    val Move: ImageVector by lazy {
        inkIcon("InkMove") {
            stroke {
                moveTo(12f, 5.2f)
                verticalLineTo(18.8f)
                moveTo(5.2f, 12f)
                horizontalLineTo(18.8f)
                moveTo(9.6f, 7.6f)
                lineTo(12f, 5.2f)
                lineTo(14.4f, 7.6f)
                moveTo(9.6f, 16.4f)
                lineTo(12f, 18.8f)
                lineTo(14.4f, 16.4f)
                moveTo(7.6f, 9.6f)
                lineTo(5.2f, 12f)
                lineTo(7.6f, 14.4f)
                moveTo(16.4f, 9.6f)
                lineTo(18.8f, 12f)
                lineTo(16.4f, 14.4f)
            }
        }
    }

    val Delete: ImageVector by lazy {
        inkIcon("InkDelete") {
            stroke {
                moveTo(5.2f, 7.2f)
                horizontalLineTo(18.8f)
                moveTo(9.6f, 7.2f)
                verticalLineTo(6f)
                curveTo(9.6f, 5.2f, 10.2f, 4.6f, 11f, 4.6f)
                horizontalLineTo(13f)
                curveTo(13.8f, 4.6f, 14.4f, 5.2f, 14.4f, 6f)
                verticalLineTo(7.2f)
                moveTo(6.6f, 7.2f)
                lineTo(7.4f, 18.2f)
                curveTo(7.5f, 19f, 8.1f, 19.5f, 8.9f, 19.5f)
                horizontalLineTo(15.1f)
                curveTo(15.9f, 19.5f, 16.5f, 19f, 16.6f, 18.2f)
                lineTo(17.4f, 7.2f)
                moveTo(10.3f, 10.6f)
                verticalLineTo(16.1f)
                moveTo(13.7f, 10.6f)
                verticalLineTo(16.1f)
            }
        }
    }

    val Purchased: ImageVector by lazy {
        inkIcon("InkPurchased") {
            stroke {
                moveTo(6.6f, 19.5f)
                verticalLineTo(5.7f)
                curveTo(6.6f, 5f, 7.1f, 4.5f, 7.8f, 4.5f)
                horizontalLineTo(16.2f)
                curveTo(16.9f, 4.5f, 17.4f, 5f, 17.4f, 5.7f)
                verticalLineTo(19.5f)
                lineTo(15.6f, 18.1f)
                lineTo(13.8f, 19.5f)
                lineTo(12f, 18.1f)
                lineTo(10.2f, 19.5f)
                lineTo(8.4f, 18.1f)
                close()
                moveTo(9.6f, 9f)
                horizontalLineTo(14.4f)
                moveTo(9.6f, 12.2f)
                horizontalLineTo(12.8f)
            }
        }
    }

    val Calendar: ImageVector by lazy {
        inkIcon("InkCalendar") {
            stroke {
                moveTo(4.6f, 8.6f)
                curveTo(4.6f, 7.2f, 5.7f, 6.1f, 7.1f, 6.1f)
                horizontalLineTo(16.9f)
                curveTo(18.3f, 6.1f, 19.4f, 7.2f, 19.4f, 8.6f)
                verticalLineTo(17f)
                curveTo(19.4f, 18.4f, 18.3f, 19.5f, 16.9f, 19.5f)
                horizontalLineTo(7.1f)
                curveTo(5.7f, 19.5f, 4.6f, 18.4f, 4.6f, 17f)
                close()
                moveTo(8.6f, 4.2f)
                verticalLineTo(7.8f)
                moveTo(15.4f, 4.2f)
                verticalLineTo(7.8f)
                moveTo(4.6f, 10.6f)
                horizontalLineTo(19.4f)
            }
            dot(12f, 14.9f, 1.25f)
        }
    }

    val Appearance: ImageVector by lazy {
        inkIcon("InkAppearance") {
            stroke {
                circle(12f, 12f, 7.4f)
            }
            // filled right half = "half-moon" appearance glyph
            path(fill = INK) {
                moveTo(12f, 4.6f)
                arcTo(7.4f, 7.4f, 0f, true, true, 12f, 19.4f)
                close()
            }
        }
    }

    val Terms: ImageVector by lazy {
        inkIcon("InkTerms") {
            stroke {
                moveTo(6.4f, 6.9f)
                curveTo(6.4f, 5.6f, 7.4f, 4.6f, 8.7f, 4.6f)
                horizontalLineTo(13.8f)
                lineTo(17.6f, 8.4f)
                verticalLineTo(17.1f)
                curveTo(17.6f, 18.4f, 16.6f, 19.4f, 15.3f, 19.4f)
                horizontalLineTo(8.7f)
                curveTo(7.4f, 19.4f, 6.4f, 18.4f, 6.4f, 17.1f)
                close()
                moveTo(13.8f, 4.6f)
                verticalLineTo(8.4f)
                horizontalLineTo(17.6f)
                moveTo(9.4f, 12.2f)
                horizontalLineTo(14.6f)
                moveTo(9.4f, 15.2f)
                horizontalLineTo(13f)
            }
        }
    }

    val Policy: ImageVector by lazy {
        inkIcon("InkPolicy") {
            stroke {
                moveTo(12f, 4.4f)
                lineTo(18.6f, 6.9f)
                verticalLineTo(11.8f)
                curveTo(18.6f, 15.6f, 16.1f, 18.3f, 12f, 19.6f)
                curveTo(7.9f, 18.3f, 5.4f, 15.6f, 5.4f, 11.8f)
                verticalLineTo(6.9f)
                close()
                moveTo(9.4f, 11.9f)
                lineTo(11.3f, 13.8f)
                lineTo(14.8f, 10f)
            }
        }
    }

    val HelpCentre: ImageVector by lazy {
        inkIcon("InkHelpCentre") {
            stroke {
                circle(12f, 12f, 7.5f)
                moveTo(9.7f, 9.8f)
                curveTo(9.9f, 8.6f, 10.9f, 7.9f, 12.1f, 8f)
                curveTo(13.3f, 8.1f, 14.2f, 9f, 14.2f, 10.1f)
                curveTo(14.2f, 11.6f, 12f, 11.8f, 12f, 13.4f)
            }
            dot(12f, 16.1f, 1.05f)
        }
    }

    val Card: ImageVector by lazy {
        inkIcon("InkCard") {
            stroke {
                moveTo(4.6f, 8f)
                curveTo(4.6f, 6.9f, 5.5f, 6f, 6.6f, 6f)
                horizontalLineTo(17.4f)
                curveTo(18.5f, 6f, 19.4f, 6.9f, 19.4f, 8f)
                verticalLineTo(16f)
                curveTo(19.4f, 17.1f, 18.5f, 18f, 17.4f, 18f)
                horizontalLineTo(6.6f)
                curveTo(5.5f, 18f, 4.6f, 17.1f, 4.6f, 16f)
                close()
                moveTo(4.6f, 9.8f)
                horizontalLineTo(19.4f)
                moveTo(7.4f, 14.4f)
                horizontalLineTo(11f)
            }
        }
    }

    val Apple: ImageVector by lazy {
        ImageVector.Builder(
            name = "InkApple",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = INK) {
                // apple body
                moveTo(16.6f, 12.9f)
                curveTo(16.6f, 10.8f, 18.3f, 9.8f, 18.4f, 9.7f)
                curveTo(17.5f, 8.4f, 16.0f, 8.2f, 15.5f, 8.2f)
                curveTo(14.2f, 8.1f, 13.0f, 9.0f, 12.4f, 9.0f)
                curveTo(11.7f, 9.0f, 10.8f, 8.2f, 9.7f, 8.3f)
                curveTo(8.3f, 8.3f, 7.0f, 9.1f, 6.3f, 10.4f)
                curveTo(4.9f, 12.9f, 5.9f, 16.5f, 7.3f, 18.5f)
                curveTo(8.0f, 19.5f, 8.8f, 20.6f, 9.8f, 20.5f)
                curveTo(10.8f, 20.5f, 11.2f, 19.9f, 12.4f, 19.9f)
                curveTo(13.6f, 19.9f, 13.9f, 20.5f, 15.0f, 20.5f)
                curveTo(16.1f, 20.5f, 16.8f, 19.5f, 17.5f, 18.5f)
                curveTo(18.3f, 17.3f, 18.6f, 16.2f, 18.6f, 16.1f)
                curveTo(18.6f, 16.1f, 16.6f, 15.3f, 16.6f, 12.9f)
                close()
                // leaf
                moveTo(14.6f, 6.9f)
                curveTo(15.2f, 6.2f, 15.5f, 5.3f, 15.4f, 4.3f)
                curveTo(14.6f, 4.4f, 13.7f, 4.9f, 13.1f, 5.6f)
                curveTo(12.6f, 6.2f, 12.2f, 7.1f, 12.3f, 8.0f)
                curveTo(13.2f, 8.1f, 14.0f, 7.6f, 14.6f, 6.9f)
                close()
            }
        }.build()
    }

    val Bell: ImageVector by lazy {
        inkIcon("InkBell") {
            stroke {
                moveTo(12f, 4.4f)
                curveTo(8.8f, 4.4f, 6.6f, 6.9f, 6.6f, 10.1f)
                verticalLineTo(13.2f)
                curveTo(6.6f, 14.6f, 6f, 15.6f, 5f, 16.6f)
                horizontalLineTo(19f)
                curveTo(18f, 15.6f, 17.4f, 14.6f, 17.4f, 13.2f)
                verticalLineTo(10.1f)
                curveTo(17.4f, 6.9f, 15.2f, 4.4f, 12f, 4.4f)
                close()
                moveTo(10.2f, 19.1f)
                arcTo(2.1f, 2.1f, 0f, false, false, 13.8f, 19.1f)
            }
        }
    }

    val Premium: ImageVector by lazy {
        inkIcon("InkPremium") {
            stroke {
                moveTo(5.5f, 17.4f)
                lineTo(4.4f, 8.6f)
                lineTo(8.9f, 11.7f)
                lineTo(12f, 6.4f)
                lineTo(15.1f, 11.7f)
                lineTo(19.6f, 8.6f)
                lineTo(18.5f, 17.4f)
                close()
            }
        }
    }

    val Send: ImageVector by lazy {
        inkIcon("InkSend") {
            stroke {
                moveTo(19.8f, 4.2f)
                lineTo(4.4f, 10.6f)
                lineTo(10.8f, 13.2f)
                lineTo(13.4f, 19.6f)
                close()
                moveTo(10.8f, 13.2f)
                lineTo(19.8f, 4.2f)
            }
        }
    }

    val Chat: ImageVector by lazy {
        inkIcon("InkChat") {
            stroke {
                bubble()
                moveTo(8.4f, 9f)
                horizontalLineTo(15.6f)
                moveTo(8.4f, 12f)
                horizontalLineTo(12.8f)
            }
        }
    }

    val Message: ImageVector by lazy {
        inkIcon("InkMessage") {
            stroke { bubble() }
            dot(8.8f, 10.3f, 1.05f)
            dot(12f, 10.3f, 1.05f)
            dot(15.2f, 10.3f, 1.05f)
        }
    }

    val Settings: ImageVector by lazy {
        inkIcon("InkSettings") {
            stroke {
                moveTo(4.4f, 7.2f)
                horizontalLineTo(12.4f)
                circle(14.6f, 7.2f, 2.2f)
                moveTo(16.8f, 7.2f)
                horizontalLineTo(19.6f)
                moveTo(4.4f, 12f)
                horizontalLineTo(6.8f)
                circle(9f, 12f, 2.2f)
                moveTo(11.2f, 12f)
                horizontalLineTo(19.6f)
                moveTo(4.4f, 16.8f)
                horizontalLineTo(13.2f)
                circle(15.4f, 16.8f, 2.2f)
                moveTo(17.6f, 16.8f)
                horizontalLineTo(19.6f)
            }
        }
    }

    val ChevronDown: ImageVector by lazy {
        inkIcon("InkChevronDown") {
            stroke {
                moveTo(7f, 9.6f)
                lineTo(12f, 14.4f)
                lineTo(17f, 9.6f)
            }
        }
    }

    private fun PathBuilder.bubble() {
        moveTo(4.4f, 8.3f)
        curveTo(4.4f, 6.2f, 6.1f, 4.6f, 8.2f, 4.6f)
        horizontalLineTo(15.8f)
        curveTo(17.9f, 4.6f, 19.6f, 6.2f, 19.6f, 8.3f)
        verticalLineTo(12.3f)
        curveTo(19.6f, 14.4f, 17.9f, 16f, 15.8f, 16f)
        horizontalLineTo(10.2f)
        lineTo(6.6f, 19.1f)
        curveTo(5.8f, 19.8f, 4.4f, 19.3f, 4.4f, 18.2f)
        close()
    }

    val Share: ImageVector by lazy {
        inkIcon("InkShare") {
            stroke {
                moveTo(12f, 14.4f)
                verticalLineTo(4.8f)
                moveTo(8.6f, 8.2f)
                lineTo(12f, 4.8f)
                lineTo(15.4f, 8.2f)
                moveTo(8f, 11.2f)
                horizontalLineTo(6.1f)
                curveTo(5.3f, 11.2f, 4.7f, 11.8f, 4.7f, 12.6f)
                verticalLineTo(18f)
                curveTo(4.7f, 18.8f, 5.3f, 19.4f, 6.1f, 19.4f)
                horizontalLineTo(17.9f)
                curveTo(18.7f, 19.4f, 19.3f, 18.8f, 19.3f, 18f)
                verticalLineTo(12.6f)
                curveTo(19.3f, 11.8f, 18.7f, 11.2f, 17.9f, 11.2f)
                horizontalLineTo(16f)
            }
        }
    }

    val Favorite: ImageVector by lazy {
        inkIcon("InkFavorite") {
            stroke {
                moveTo(12f, 19.1f)
                curveTo(7.3f, 15.8f, 4.5f, 13f, 4.5f, 9.9f)
                curveTo(4.5f, 7.4f, 6.3f, 5.6f, 8.6f, 5.6f)
                curveTo(10f, 5.6f, 11.3f, 6.3f, 12f, 7.5f)
                curveTo(12.7f, 6.3f, 14f, 5.6f, 15.4f, 5.6f)
                curveTo(17.7f, 5.6f, 19.5f, 7.4f, 19.5f, 9.9f)
                curveTo(19.5f, 13f, 16.7f, 15.8f, 12f, 19.1f)
                close()
            }
        }
    }

    val MoreVertical: ImageVector by lazy {
        inkIcon("InkMoreVertical") {
            dot(12f, 5.8f, 1.3f)
            dot(12f, 12f, 1.3f)
            dot(12f, 18.2f, 1.3f)
        }
    }

    private fun PathBuilder.square(x: Float, y: Float, s: Float) {
        moveTo(x, y)
        horizontalLineTo(x + s)
        verticalLineTo(y + s)
        horizontalLineTo(x)
        close()
    }

    private fun PathBuilder.eyeOutline() {
        moveTo(4.6f, 12f)
        curveTo(6.6f, 8.3f, 9.1f, 6.6f, 12f, 6.6f)
        curveTo(14.9f, 6.6f, 17.4f, 8.3f, 19.4f, 12f)
        curveTo(17.4f, 15.7f, 14.9f, 17.4f, 12f, 17.4f)
        curveTo(9.1f, 17.4f, 6.6f, 15.7f, 4.6f, 12f)
        close()
    }

    private fun PathBuilder.circle(cx: Float, cy: Float, r: Float) {
        moveTo(cx - r, cy)
        arcToRelative(r, r, 0f, true, false, 2 * r, 0f)
        arcToRelative(r, r, 0f, true, false, -2 * r, 0f)
        close()
    }
}

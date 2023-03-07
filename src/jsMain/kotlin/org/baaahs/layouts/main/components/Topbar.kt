package org.baaahs.layouts.main.components

import csstype.AlignItems
import csstype.Auto
import csstype.BorderColor
import csstype.Display
import csstype.JustifyContent
import csstype.None
import csstype.px
import externals.mui.material.styles.alpha
import mui.material.Box
import mui.material.Button
import mui.material.ButtonColor
import mui.material.ButtonVariant
import mui.material.Link
import mui.material.LinkUnderline
import mui.material.PaletteMode
import mui.material.Size
import mui.material.styles.Theme
import mui.material.styles.useTheme
import mui.system.sx
import org.baaahs.layouts.Page
import org.baaahs.layouts.main.components.topbar.NavItem
import org.baaahs.util.breakpoints
import org.baaahs.util.sp
import react.FC
import react.Props
import react.dom.aria.ariaLabel
import react.dom.html.AnchorHTMLAttributes
import react.dom.html.ImgHTMLAttributes
import react.dom.html.ReactHTML.a
import react.dom.html.ReactHTML.img
import web.html.HTMLAnchorElement
import web.html.HTMLImageElement
import web.window.WindowTarget

val Topbar = FC<TopbarProps> { props ->
    val colorInvert = props.colorInvert ?: false

    val theme = useTheme<Theme>()
    val mode = theme.palette.mode
    val landingPages = props.pages["landings"] ?: error("no landing pages")
    val companyPages = props.pages["company"] ?: error("no company pages")

    Box {
        sx {
            display = Display.flex
            justifyContent = JustifyContent.spaceBetween
            alignItems = AlignItems.center
            width = 1.sp
        }
        Box {
            component = a; this as AnchorHTMLAttributes<HTMLAnchorElement>
            href = "/"
            title = "theFront"

            sx {
                display = Display.flex
                width = breakpoints { xs = 100.px; md = 120.px }
            }

            Box {
                component = img; this as ImgHTMLAttributes<HTMLImageElement>
                src = if (mode == PaletteMode.light && !colorInvert) {
                    "https://assets.maccarianagency.com/the-front/logos/logo.svg"
                } else {
                    "https://assets.maccarianagency.com/the-front/logos/logo-negative.svg"
                }
                height = 1.sp
                width = 1.sp
            }
        }
        Box {
            sx {
                display = breakpoints { xs = None.none; md = Display.flex }
                alignItems = AlignItems.center
            }

            Box {
                sx { marginRight = breakpoints { xs = 1.sp; sm = 2.sp } }

                Link {
                    underline = LinkUnderline.none
                    component = a
                    href = "/events"
                    color = if (colorInvert) "common.white" else "text.primary"
                    sx {
                        display = Display.flex
                        alignItems = AlignItems.center
                    }

                    +"events"
                }
            }
            Box {
                sx { marginRight = breakpoints { xs = 1.sp; sm = 2.sp } }

                Link {
                    underline = LinkUnderline.none
                    component = a
                    href = "/music"
                    color = if (colorInvert) "common.white" else "text.primary"
                    sx {
                        display = Display.flex
                        alignItems = AlignItems.center
                    }

                    +"music"
                }
            }
            Box {
                sx { marginRight = breakpoints { xs = 1.sp; sm = 2.sp } }

                Link {
                    underline = LinkUnderline.none
                    component = a
                    href = "/fundraising"
                    color = if (colorInvert) "common.white" else "text.primary"
                    sx {
                        display = Display.flex
                        alignItems = AlignItems.center
                    }

                    +"fundraising"
                }
            }
            Box {
                sx { marginRight = breakpoints { xs = 1.sp; sm = 2.sp } }

                Link {
                    underline = LinkUnderline.none
                    component = a
                    href = "/about"
                    color = if (colorInvert) "common.white" else "text.primary"
                    sx {
                        display = Display.flex
                        alignItems = AlignItems.center
                    }

                    +"about"
                }
            }
        }
        Box {
            sx {
                display = breakpoints { xs = None.none; md = Display.flex }
                alignItems = AlignItems.end
            }

            Box {
                NavItem {
                    title = "soundcloud"
                    id = "landing-pages"
                    items = landingPages
                    this.colorInvert = colorInvert
                }
            }
            Box {
                sx {
                    marginLeft = 4.sp
                }
                NavItem {
                    title = "email"
                    id = "company-pages"
                    items = companyPages
                    this.colorInvert = colorInvert
                }
            }
            Box {
                sx { marginLeft = 4.sp }
                Button {
                    variant = ButtonVariant.contained
                    color = ButtonColor.primary
                    asDynamic().component = a; this as AnchorHTMLAttributes<HTMLAnchorElement>
                    target = WindowTarget._blank
                    href = "/crew/"
                    size = Size.large
                    +"Crew login"
                }
            }
        }
        Box {
            sx {
                display = breakpoints {
                    xs = Display.block
                    md = None.none
                }
                alignItems = AlignItems.center
            }

            Button {
                onClick = { props.onSidebarOpen() }
                ariaLabel = "Menu"
                variant = ButtonVariant.outlined
                sx {
                    borderRadius = 2.sp
                    minWidth = Auto.auto
                    padding = 1.sp
                    borderColor = alpha(theme.palette.divider, 0.2) as BorderColor
                }

                mui.icons.material.Menu {}
            }
        }
    }
}

external interface TopbarProps : Props {
    var onSidebarOpen: (() -> Unit)
    var pages: Map<String, List<Page>>
    var colorInvert: Boolean?
}
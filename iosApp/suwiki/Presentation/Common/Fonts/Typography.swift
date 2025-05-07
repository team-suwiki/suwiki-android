//
//  Typography.swift
//  suwiki
//
//  Created by 이진욱 on 5/8/25.
//

import SwiftUI

let Typography = SuwikiTypography(
    header1: TextStyle(font: .notoSans(.bold, size: 22), lineSpacing: 1.5, color: .black),
    header2: TextStyle(font: .notoSans(.bold, size: 18), lineSpacing: 1.5, color: .black),
    header3: TextStyle(font: .notoSans(.medium, size: 18), lineSpacing: 1.5, color: .black),
    header4: TextStyle(font: .notoSans(.regular, size: 18), lineSpacing: 1.5, color: .black),
    header5: TextStyle(font: .notoSans(.bold, size: 16), lineSpacing: 1.5, color: .black),
    header6: TextStyle(font: .notoSans(.medium, size: 16), lineSpacing: 1.5, color: .black),
    header7: TextStyle(font: .notoSans(.regular, size: 16), lineSpacing: 1.5, color: .black),
    
    body1: TextStyle(font: .notoSans(.bold, size: 15), lineSpacing: 1.5, color: .black),
    body2: TextStyle(font: .notoSans(.medium, size: 15), lineSpacing: 1.5, color: .black),
    body3: TextStyle(font: .notoSans(.regular, size: 15), lineSpacing: 1.5, color: .black),
    body4: TextStyle(font: .notoSans(.medium, size: 14), lineSpacing: 1.5, color: .black),
    body5: TextStyle(font: .notoSans(.regular, size: 14), lineSpacing: 1.5, color: .black),
    body6: TextStyle(font: .notoSans(.medium, size: 13), lineSpacing: 1.5, color: .black),
    body7: TextStyle(font: .notoSans(.regular, size: 13), lineSpacing: 1.5, color: .black),

    caption1: TextStyle(font: .notoSans(.medium, size: 12), lineSpacing: 1.5, color: .black),
    caption2: TextStyle(font: .notoSans(.regular, size: 12), lineSpacing: 1.5, color: .black),
    caption3: TextStyle(font: .notoSans(.bold, size: 11), lineSpacing: 1.5, color: .black),
    caption4: TextStyle(font: .notoSans(.regular, size: 11), lineSpacing: 1.5, color: .black),
    caption5: TextStyle(font: .notoSans(.medium, size: 10), lineSpacing: 1.5, color: .black),
    caption6: TextStyle(font: .notoSans(.regular, size: 10), lineSpacing: 1.5, color: .black),
    caption7: TextStyle(font: .notoSans(.regular, size: 8), lineSpacing: 1.5, color: .black)
)


struct SuwikiTypography {
    let header1: TextStyle
    let header2: TextStyle
    let header3: TextStyle
    let header4: TextStyle
    let header5: TextStyle
    let header6: TextStyle
    let header7: TextStyle

    let body1: TextStyle
    let body2: TextStyle
    let body3: TextStyle
    let body4: TextStyle
    let body5: TextStyle
    let body6: TextStyle
    let body7: TextStyle

    let caption1: TextStyle
    let caption2: TextStyle
    let caption3: TextStyle
    let caption4: TextStyle
    let caption5: TextStyle
    let caption6: TextStyle
    let caption7: TextStyle
}

struct TextStyle {
    let font: Font
    let lineSpacing: CGFloat
    let color: Color
}

struct TypographyModifier: ViewModifier {
    let style: TextStyle

    func body(content: Content) -> some View {
        content
            .font(style.font)
            .lineSpacing(style.lineSpacing)
            .foregroundColor(style.color)
    }
}

extension View {
    func typography(_ style: TextStyle) -> some View {
        self.modifier(TypographyModifier(style: style))
    }
}


extension Font {
    static func notoSans(_ weight: Font.Weight, size: CGFloat) -> Font {
        let fontName: String
        switch weight {
        case .bold: fontName = "NotoSansKR-Bold"
        case .medium: fontName = "NotoSansKR-Medium"
        case .regular: fontName = "NotoSansKR-Regular"
        case .light: fontName = "NotoSansKR-Light"
        default: fontName = "NotoSansKR-Regular"
        }
        return .custom(fontName, size: size)
    }
}

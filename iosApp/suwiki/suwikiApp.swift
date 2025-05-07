//
//  suwikiApp.swift
//  suwiki
//
//  Created by 이진욱 on 4/20/25.
//

import SwiftUI

@main
struct suwikiApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView()
            .environmentObject(TypographyTheme())
        }
    }
}

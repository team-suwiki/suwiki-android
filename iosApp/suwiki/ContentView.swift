//
//  ContentView.swift
//  suwiki
//
//  Created by 이진욱 on 4/20/25.
//

import SwiftUI
import sharedKit

struct ContentView: View {
  @EnvironmentObject var typography: TypographyTheme
  
    var body: some View {
        VStack {
            Image(systemName: "globe")
                .imageScale(.large)
                .foregroundStyle(.tint)
          Text(Greeting().greet())
            .foregroundColor(AppColor.grayDA)
            .typography(typography.caption1)
        }
        .padding()
    }
}

#Preview {
    ContentView()
    .environmentObject(TypographyTheme())
}

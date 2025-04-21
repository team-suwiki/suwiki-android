//
//  ContentView.swift
//  suwiki
//
//  Created by 이진욱 on 4/20/25.
//

import SwiftUI
import sharedKit

struct ContentView: View {
    var body: some View {
        VStack {
            Image(systemName: "globe")
                .imageScale(.large)
                .foregroundStyle(.tint)
          Text(Greeting().greet())
        }
        .padding()
    }
}

#Preview {
    ContentView()
}

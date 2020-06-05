// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Adds a random fact about myself or random quote from one of my favorite shows.
 */
function addRandomGreeting() {
  const greetings =
      ['Fun Fact: My all-time favorite TV show is Dragon Ball Z.', 
      'Fun Fact: I enjoy reading manga, which are japanese styled comic books, like My Hero Acedamia, Death Note, Bakuman, Jujutsu Kaisen, Demon Slayer:Kimetsu no Yaiba, Bleach, etc.',
       '“You’re absolutely right. I can’t do anything alone. Everyone has their flaws and imperfections, but that’s what drives us to work together… To make up for those flaws. Together, we make the perfect main character.” – Gintoki Sakata',
        'Fun Fact: I walked next to a lion.'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

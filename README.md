<p align="center">
<img width="490" height="270" alt="Ace-Logo" src="https://github.com/user-attachments/assets/1ea4f2fc-309f-461b-a3f5-5924f603d8ce" />
</p>

# Bitwise Bandits — Ace Hardware Multi-Store Website Redesign

## Overview
Bitwise Bandits is redesigning and modernizing the multi-store website for Ace Hardware locations owned by our client, Greg Werner. Their <a href="https://granitebayace.com/"> current Ace Hardware website <a/> is outdated, difficult to maintain, visually inconsistent, and extremely expensive to host. Our mission is to deliver a modern, fast, clean, and cost-efficient website that provides accurate store information while reducing long-term hosting burden.

## Project Synopsis
This project rebuilds Ace Hardware’s multi-location website from scratch using modern web practices. We aim to:
- Replace the expensive legacy hosting with a low-cost (sub–$150/yr) solution
- Create modular store components
- Build a visually consistent, professional front end
- Support easy expansion as store count increases

### How we Rebuilt the Website
- Identified outdated layout patterns, inconsistent styling, and navigation gaps, such as only Facebook is the official social media account, and customers have to click on a link to see a PDF file for Specials
- Mapped all existing pages, images, and content while talking to the client via Zoom to understand what they needed preserved
- Reviewed the performance issues and hosting limitations of the legacy site
- Designed a prototype using Figma and showed it to the client for approval.

### 🖥️ Front End Solution
- Reorganized all existing content into separate, clearly defined pages to improve navigation and readability.​
- Implemented a responsive navigation bar for consistent access to pages across all screen sizes.​
- Added dropdown menus to display detailed information in a clean, compact format.​
- Redesigned headers and footers to create a modern, cohesive look throughout the site.​
- Added designated spaces for advertisements on the homepage, allowing them to be easily added, edited, or updated as needed.​
- Enhanced the overall user interface and visual hierarchy, aligning the website with modern web design standards and improving accessibility.

### 🛠️ Back End Solution
- Allow existing admins to control user roles and permissions more efficiently​
- Allow admins to manage advertisements​
- Storing admin-related info in a database

## Current Website:

# We decided to divide the original site into pages so each page only needs to carry one functionality.

<img width="1910" height="1032" alt="Screenshot 2025-11-30 145008" src="https://github.com/user-attachments/assets/4d51fe88-aa1e-44ac-8026-90c071ecf1b4" />
*Home Page: We brought the advertisements to the website interface rather than having customers open a PDF file. In Spring 2026, we will develop a functionality that lets the owner dynamically change the advertisements*

<img width="1918" height="831" alt="image" src="https://github.com/user-attachments/assets/cf19f793-011c-432f-9569-81319c5b1e81" />
*About Page: We preferred to put the content in the middle of the page rather than at the page's bottom like the original page*

<img width="1917" height="936" alt="image" src="https://github.com/user-attachments/assets/7d48cabd-b6bb-4975-a346-9ea36de743ec" />
*Location Page: Will have locations, their addresses, phone numbers, and opening hours*

<img width="1917" height="942" alt="image" src="https://github.com/user-attachments/assets/432cad5e-d139-4a53-8b91-5ff9cc0d5be9" />
*Admin Page: Has more functionalities to manage employees' accounts and dynamically update advertisements*

<img width="1917" height="940" alt="image" src="https://github.com/user-attachments/assets/ac92065a-e554-4312-a228-e53d80db495e" />
*Admin Login Page: A basic style login page where the admins will log in for extended functionalities*

## Tech Stack
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-%231B72BE.svg?style=for-the-badge&logo=intellij-idea&logoColor=white) ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) ![JavaScript](https://img.shields.io/badge/javascript-%23323330.svg?style=for-the-badge&logo=javascript&logoColor=%23F7DF1E) ![SQLite](https://img.shields.io/badge/sqlite-%2307405e.svg?style=for-the-badge&logo=sqlite&logoColor=white) ![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white) 
- IDE 
  - IntelliJ 
- Frontend​
  - HTML, CSS, JavaScript​
- Framework
  - Java
- Backend​
  - SQLite ​
- API’s ​
  - N/A​
- Version Control​
  - Git/Github​
- Servers​/Server Cost
  - Estimated cost for Amazon Lightsail:​
  - Domain cost: $10-20/year​
  - Backend/DB cost: $0.0047/hour ($3.50/mo)​
  - Framework hosting: Amazon Lightsail deployed with Docker Images
 
## Prototype With Figma
![ace-prototype](https://github.com/user-attachments/assets/339b8f31-461a-4d4f-b04c-23144588b99b)

## Application Flow
![ace-flow](https://github.com/user-attachments/assets/f1d317e7-5fe5-4ce4-a4c4-3ace6313f377)

## Timeline (CSC 191 Based on JIRA)

### Week 1
- Repo prep and environment setup

### Week 2
- Backend architecture and API design

### Week 3
- Frontend to backend integration

### Week 4
- Admin tools and authentication

### Week 5
- Deployment prep and CI/CD

### Week 6
- Testing (unit, integration, load)

### Week 7
- Client review, polish

### Week 8
- Final deployment + handoff

## Developer Setup (Placeholder for CSC 191)
```
git clone https://github.com/SpencerNold/Granite-Bay-Ace-Website.git
cd Granite-Bay-Ace-Website

# For PowerShell, ZShell, Bash, and other POSIX shells
./gradlew run

# For Windows Command Prompt
./gradlew.bat run
```


## Testing (Placeholder)
To be completed in CSC 191.

## Deployment (Placeholder)
To be completed in CSC 191.



## Team
<a href="https://github.com/SpencerNold/Granite-Bay-Ace-Website/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=SpencerNold/Granite-Bay-Ace-Website" />
</a> 
<p></p>

* Matthew Farr (Lead)
* Spencer Nold
* Daniel Balolong
* Alyssa Jimenez
* Arsal Mahmood
* Timothy Talampas
* William Yap
* Nguyen Ho

Client: Greg Werner

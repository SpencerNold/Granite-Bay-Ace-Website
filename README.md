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
- Implemented Google Maps API for intuitive user interaction

## Current Website:

### We decided to divide the original site into multiple different pages to make information easier to digest.

<img src="Readme_assets/Home.png" width = 2753 height = 1506 alt="home">
<strong> Home Page:</strong> The home page has been reworked to now display each stores' information on rotation. It also the place to view monthly deals and join the Ace Hardware rewards program.
<br><br><br>

<img src="Readme_assets/About-us.png" width = 2744 height = 1397 alt="home">
<strong> About Page: </strong> The about page contains basic information on the left and links to extra resources on the right.
<br><br><br>

<img src="Readme_assets/Services.png" width = 2746 height = 1344 alt="home">
<strong> Services Page: </strong> The services was specially requested so users can view which stores offer which services.
<br><br><br>

<img src="Readme_assets/Statements1.png" width = 2745 height = 1513 alt="home">
<img src="Readme_assets/Statements2.png" width = 2752 height = 1268 alt="home">
<strong> Statements Page: </strong> All the statements are now integrated into a singular page to make their respective information easier to access.
<br><br><br>

<img src="Readme_assets/Login.png" width = 2759 height = 724 alt="home">
<strong> Login Page: </strong> A basic login page for both admin and managers.
<br><br><br>

<img src="Readme_assets/Manage-Acc.png" width = 1215 height = 489 alt="home">
<strong> Manager Account Page: </strong> The landing page for the admin/manager upon successful login. It is design to contain all major functionalities in one place.
<br><br><br>

<img src="Readme_assets/Recover.png" width = 1139 height = 625 alt="home">
<strong> Recover Account Page: </strong> Accessible to both admins and managers but only interactable by admins, this is a dedicated page to reset account password.
<br>

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
- Testing Framework
  - JUnit5
- Servers​/Server Cost
  - Estimated cost for Amazon Lightsail:​
  - Domain cost: $10-20/year​
  - Backend/DB cost: $0.0047/hour ($3.50/mo)​
  - Framework hosting: Amazon Lightsail deployed with Docker Images
 
## Prototype With Figma
![ace-prototype](https://github.com/user-attachments/assets/339b8f31-461a-4d4f-b04c-23144588b99b)

## Application Flow
![ace-flow](https://github.com/user-attachments/assets/f1d317e7-5fe5-4ce4-a4c4-3ace6313f377)

## Developer Setup
The following needs to be installed on a computer to properly start development work on the website.

<strong> Java <strong>
1. Any runtime environment following the Oracle Java 21 specifications needs to be installed and referenced in the PATH/JAVA_HOME environment variables of the system.
2. Installation of Java 21 can be validated by executing ‘java —version’ in the command-line.
<img src="Readme_assets/Deployment1.png" width = 780 height = 122 alt="home">
<br><br>

<strong> Git <strong>
1. GitHub Desktop, git cli, or any way of cloning, committing, pushing, etc. needs to be installed on the system for development.
<img src="Readme_assets/Deployment2.png" width = 580 height = 381 alt="home">
<br><br>

<strong> IDE <strong>
1. Any Java IDE which supports Gradle 7.3.3 with Kotlin for the DSL 1.5.31 can be used for development (Eclipse, IntelliJ, VSCode, etc.).
<br>

## Testing
Running all automated test:

1. Open a terminal window in your respective IDE.
<img src="Readme_assets/Testing1.png" width = 944 height = 125 alt="home">
<br><br>
2. Run ‘./gradlew test’ in the terminal.
<img src="Readme_assets/Testing2.png" width = 980 height = 308 alt="home">
<br><br>

Running a certain automated test:

1. Open a terminal window in your respective IDE.
<img src="Readme_assets/Testing1.png" width = 944 height = 125 alt="home">
<br><br>
2. Run ‘./gradlew  test <Test Name>’ in the terminal.
<img src="Readme_assets/Testing3.png" width = 1058 height = 521 alt="home">
<br><br>
3. To look at the different integration and unit tests please navigate to src/test/routes or src/test/unit. 
<br>

## Deployment
Since the client intends to self-host, the final production deployment of the website is handled by the client’s IT team. Once deployed by the IT team, the website should be accessible through a live public URL with interactive pages for customers along with functioning admin operations.
<br><br>
To demonstrate deployment, we will instead show the hosting strategy that we will use for our senior project showcase. This involves hosting the application in a controlled environment using the following steps.

<Strong> Preparation for Deployment <Strong>
Ensure all traffic is secure and aligns with standard secure web practices.
- Update the server mode from HTTP to HTTPS
- Change the port from 80 to 443
<br>

<Strong> Build the Application Distribution <strong>
Generate a packaged version of the product that can be deployed on another device.
- In the preferred IDE (we are using IntelliJ), navigate to the designated project folder, replacing <path/to/project> with your project path.
<img src="Readme_assets/Deploy1.png" width = 1194 height = 50 alt="home">

<br>
- Run the following command: ./gradlew distZip
<img src="Readme_assets/Deploy2.png" width = 1147 height = 57 alt="home">
<br><br>

<strong> Transfer and Extract the Build <strong>
Prepare the application files on the device where it will be hosted.
- In your project directory, navigate to build/distributions to find the generated ZIP file in your file explorer located at the following location.
<img src="Readme_assets/Deploy3.png" width = 1263 height = 226 alt="home">
<br><br>

<strong> Configure Security Certificates <strong>
Enable HTTPS by allowing the server to encrypt incoming and outgoing traffic.
- Place the TLS certificate into the same directory/folder as the application files
<br>

<strong> Start the Application <strong>
Initialize the application and begin serving requests.
- From the contents in the extracted ZIP file, navigate to Granite-Bay-Ace-Website/bin
- Execute the start script to launch the server
<br>

<strong> Configure Network Access <strong>
Allow external users to access the hosted application securely.
- Open port 443 on the device’s firewall
- Ensure the network allows for inbound connections

<strong> Configure Domain Routing <strong>
Allow users to access the application using a domain instead of an IP address.
- Change domain names to point at the public IP address of the device
<br>

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

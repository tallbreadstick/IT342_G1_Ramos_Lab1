# Description

Small auth Spring service + React frontend + Kotlin Android client for lab activity

# Tech Stack

- Springboot + Java
- ReactJS + TypeScript + Vite
- Tailwind CSS, React Router, Axios
- Kotlin, Jetpack Compose, Android
- MySQL, XAMPP

# Steps to run backend

1. Open the 'backend' folder in Intellij IDEA
2. Setup the project SDK (make sure JDK is 19 or higher)
3. Make sure all maven deps are loaded
4. Create and run MySQL database, db name 'it342_g1_ramos_lab1', username root, password empty
5. hit run on Intellij, hope nothing breaks

# Steps to run web app

1. cd to 'web' folder
2. run npm install if not done already
3. make sure the backend server is running
4. do npm run dev and open localhost:5173 in any browser

# Steps to run mobile app

TBA, not yet implemented

# List of API endpoints

ROOT => localhost:8080

POST /api/auth/register \
POST /api/auth/login \
GET /api/user/me \
POST /auth/logout
# **Voterra**
## **Overview**:
Voterra is a dynamic web application designed to integrate polling into the core of social interactions. By enabling real-time voting and feedback on every post, Voterra fosters enhanced user engagement and community participation. Users can express their opinions seamlessly while staying connected with others in the community.

## **Purpose**:
The primary goal of Voterra is to create a platform where users can easily create and participate in polls, thereby promoting active involvement and interaction. This feature-rich application ensures that users have a voice and can contribute to discussions in a meaningful way.

## **Key Benefits**:
- **Enhanced Interaction**: By embedding polls in posts, Voterra encourages users to engage more actively with content.
- **Community Building**: The platform supports a sense of community by allowing users to share their opinions and see the collective feedback.
- **User Empowerment**: Voterra empowers users by giving them tools to create, vote, and interact with posts in real-time.

## **Getting Started**:
To get started with Voterra, follow the setup instructions for both the frontend and backend applications. Ensure that all dependencies are installed and configured correctly to run the development and backend servers smoothly.

## **Table of Contents**:
1. [Authors](#authors)
2. [Features](#features)
3. [Frontend App Setup](#frontend-app-setup)
4. [Backend App Setup](#backend-app-setup)
5. [Starting the Development Server](#starting-the-development-server)
6. [Starting the Backend Server](#starting-the-backend-server)
7. [Database Setup](#database-setup)
8. [Running Unit Tests](#running-unit-tests)
9. [Dependencies](#dependencies)
10. [Contact Us](#contact-us)


## **Authors**:
- [Ranime Shehata](https://github.com/ranimeshehata)
- [Karene Antoine](https://github.com/Karene-Antoine)
- [Abdallah Mohamed Ali Zain](https://github.com/Abdullh911)
- [Bahaa Khaled](https://github.com/BahaaKhaled2026)
- [Pola Hany](https://github.com/polahany)
- [Ibrahim Tareq](https://github.com/Ibrahimtareq952001)


 ## **Features**:
- **Login and Sign Up**: Users can create accounts and log in to access the platform.
- **User Authentication and Authorization**: Secure authentication and authorization mechanisms to protect user data and ensure that only authorized users can access certain features.
- **Create Posts**: Users can create posts with embedded polls.
- **Real-time Voting**: Users can vote on polls in real-time.
- **User Profiles**: Each user has a profile displaying their posts and activity.
- **Friend System**: Users can add & remove friends and view their posts.
- **Search and Filter**: Users can search for posts and filter them by category.
- **Report and Save Posts**: Users can report inappropriate posts and save posts for later.
- **Privacy Settings**: Users can adjust their privacy settings to control who can see their posts and profile.
- **Admin**: Admins have access to a Reported Posts Page where they can manage posts and maintain Voterra.
- **Advanced Search**: Users can perform advanced searches using multiple criteria.
- **Post Filtering**: Users can filter posts by category.
- **Save Posts**: Users can save posts to view them later.
- **Delete Posts**: Users can delete their own posts.
- **Additional Pages**: The application includes additional pages such as About and Contact for user assistance.


## **Frontend App Setup**:
- The frontend is built with React and Vite:
```
npm create vite@latest voterra --template react
```
```
cd frontend
```
```
npm install
```

## **Backend App Setup**:
- The backend is built with Node.js and Express:
```sh
cd backend-app
```
```sh
npm install
mvn install
```

## **Starting the Development Server**:
- After setting up, the development server can be started:
```
cd frontend-app/voterra
```
```
npm run dev
```

## **Starting the Backend Server**:
- To start the backend server, run:
```sh
mvn spring-boot:run
```

## **Database Setup**:
- The backend uses MongoDB as the database. Ensure MongoDB is installed and running on your machine. You can start MongoDB using:
```sh
mongod
```

## **Running Unit Tests**:
- To run the unit tests for the backend, use the following command:
```sh
cd backend-app
```
```sh
mvn test
```

## **Dependencies**:
- The project uses the following dependencies:
  - React
  - React Router DOM
  - Tailwind CSS
  - ESLint
  - Vite
  - Spring Boot
  - MongoDB
  - Mongoose
  - Express
  - Dotenv
  - JUnit
  - Mockito

For a complete list of dependencies, refer to the `pom.xml` and `package.json` files in the respective directories.

## **Contact Us**:
If you have any questions or feedback, feel free to reach out to us at:
- Email: voterra.app@gmail.com
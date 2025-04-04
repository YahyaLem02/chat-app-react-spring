# ChatApp - React & Spring Boot

## Description

**ChatApp** est une application de chat en temps réel construite avec **React** pour le frontend et **Spring Boot** pour le backend. Cette application permet aux utilisateurs de s'envoyer des messages en direct dans une interface simple et intuitive.

## Fonctionnalités

- **Messagerie en temps réel** : Les utilisateurs peuvent envoyer et recevoir des messages instantanément.
- **Interface utilisateur moderne** : Développée avec React pour offrir une expérience fluide.
- **Backend robuste** : Utilisation de Spring Boot pour gérer la logique métier et les requêtes API.
- **Authentification** : Système d'inscription et de connexion des utilisateurs.

## Prérequis

Avant de commencer, assurez-vous d'avoir installé les éléments suivants :

- **Node.js** (pour le frontend React)
- **Java JDK** (pour le backend Spring Boot)
- **Maven** ou **Gradle** (pour la gestion des dépendances du backend Spring)
- **Base de données** (si utilisée, PostgreSQL, MySQL, ou MongoDB)

## Installation

### Frontend (React)

1. Clonez le repository :

   ```bash
   git clone https://github.com/YahyaLem02/chat-app-react-spring.git
   cd chat-app-react-spring

    Allez dans le répertoire du frontend et installez les dépendances :

cd frontend
npm install

Démarrez le serveur de développement React :

    npm start

    Cela lancera le frontend sur http://localhost:3000.

Backend (Spring Boot)

    Allez dans le répertoire du backend et construisez le projet avec Maven ou Gradle :

    Avec Maven :

cd backend
mvn clean install

Avec Gradle :

cd backend
./gradlew build

Exécutez le projet Spring Boot :

    mvn spring-boot:run

    Le backend sera accessible à l'adresse http://localhost:8080.

    Si vous avez configuré une base de données, assurez-vous d'ajouter les variables d'environnement appropriées pour la connexion (ex. : DB_URL, DB_USER, DB_PASSWORD).

Déploiement
Déploiement du Frontend

Le frontend peut être déployé sur des plateformes comme Netlify ou Vercel. Il suffit de connecter le dépôt GitHub et de suivre les instructions pour déployer automatiquement chaque fois que vous effectuez un push sur le repository.
Déploiement du Backend

Le backend peut être déployé sur des plateformes comme Heroku, Railway, ou Render.

    Poussez votre code sur GitHub.

    Connectez votre repository à la plateforme choisie.

    Configurez les variables d'environnement nécessaires (comme la base de données, les clés API, etc.).

Technologies Utilisées

    Frontend : React.js, CSS

    Backend : Spring Boot, Spring Web, Spring Security 

    Base de données :  MySQL

    WebSocket : Pour la messagerie en temps réel

Contribuer

    Fork ce repository.

    Créez une branche pour votre fonctionnalité (git checkout -b feature/nouvelle-fonctionnalite).

    Commitez vos modifications (git commit -m 'Ajout d'une nouvelle fonctionnalité').

    Poussez votre branche (git push origin feature/nouvelle-fonctionnalite).

    Ouvrez une pull request.


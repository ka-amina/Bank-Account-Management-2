# Application Console de Gestion Bancaire Digitale

## 📋 Description

Cette application console en Java permet d'ouvrir et de gérer des comptes bancaires numériques en toute sécurité. Les utilisateurs peuvent créer différents types de comptes, effectuer dépôts, retraits, virements internes ou externes, demander et suivre des crédits avec calcul automatique des intérêts, puis consulter leur solde et l'historique complet des transactions.

Conçue comme un projet pédagogique avancé, elle met en pratique la programmation orientée objet, les collections et streams Java, ainsi qu'une architecture en couches évolutive prête pour une future intégration avec PostgreSQL ou une plateforme FinTech plus étendue.

## 🎯 Objectifs

- **Gestion complète des comptes bancaires** (Courant, Épargne, Crédit)
- **Exécution sécurisée et traçable** des transactions (dépôts, retraits, virements internes/externes)
- **Gestion avancée des crédits** avec calculs d'intérêts et échéanciers automatiques
- **Génération de rapports financiers** et statistiques en temps réel
- **Validation stricte** de toutes les opérations selon des règles métier

## ✨ Fonctionnalités Principales

### 🏦 Gestion des Comptes
- **Création de comptes** : Courant, Épargne, Crédit avec ID unique généré automatiquement
- **Gestion multi-devise** : MAD, EUR, USD avec taux de change configurables
- **Mise à jour et clôture** sécurisées des comptes
- **Découvert autorisé** selon configuration

### 💰 Gestion des Transactions
- **Dépôts et retraits** avec validation des montants
- **Virements internes et externes** avec conversion automatique de devises
- **Historisation immuable** de toutes les opérations
- **Détection d'anomalies** et respect des règles AML (Anti-Money Laundering)

### 💳 Gestion des Crédits
- **Demande de crédit** avec validation automatique (mensualité ≤ 40% du revenu)
- **Calculs d'intérêts** simples ou composés
- **Génération automatique d'échéanciers** mensuels
- **Débit automatique** du compte lié
- **Gestion des pénalités** et changement de statut automatique

### 💸 Gestion des Frais & Commissions
- **Frais configurables** par type d'opération (fixes ou pourcentage)
- **Double écriture** obligatoire (débit client + crédit compte technique)
- **Configuration flexible** des règles de facturation

### 📊 Rapports et Statistiques
- **Solde total** de la banque
- **Revenus générés** par les crédits et frais
- **Top clients** par volume d'opérations
- **Export** CSV / TXT

## 🏗️ Architecture Technique

### Technologies Utilisées
- **Java 17+**
- **PostgreSQL 15+**
- **JDBC** pour la persistance
- **Architecture en couches** : UI (console), Controller, Service, Repository, Domain

### Design Patterns Implémentés
- **Singleton** : Configuration et connexion DB
- **Repository** : Gestion de la persistance
- **MVC** : Séparation des responsabilités

### APIs et Frameworks Java
- **Collections & Streams** pour le traitement des données
- **Enums** pour les types et statuts
- **Java Time API** pour la gestion des dates
- **BigDecimal** pour les calculs monétaires précis

## 🔐 Gestion des Rôles et Droits

| Rôle | Permissions |
|------|-------------|
| **TELLER** | Opérations courantes (dépôts, retraits, virements internes, demandes crédits) |
| **MANAGER** | Validation opérations sensibles (clôtures, crédits, virements externes) |
| **ADMIN** | Tous les droits + configuration système |
| **AUDITOR** | Lecture seule pour audit et reporting |

### Variables d'Environnement Disponibles
- `DB_HOST` : Adresse du serveur PostgreSQL
- `DB_PORT` : Port de connexion (défaut: 5432)
- `DB_NAME` : Nom de la base de données
- `DB_USER` : Nom d'utilisateur de la base
- `DB_PASSWORD` : Mot de passe de la base

## 🚀 Installation et Configuration

### Prérequis
- Java 17 ou supérieur
- PostgreSQL 15+
- Maven (optionnel)
- Variables d'environnement configurées via fichier `.env`

### Configuration Variables d'Environnement

1. **Créer le fichier `.env`** à la racine du projet :
```env
DB_HOST=localhost
DB_PORT=5432
DB_NAME=banking_app
DB_USER=your_username
DB_PASSWORD=your_password
```

2. **Ajouter `.env` au `.gitignore`** pour la sécurité :
```gitignore
# Environment variables
.env
*.env
```

### Configuration Base de Données
```sql
-- Créer la base de données
CREATE DATABASE bank_management;

-- Configurer les tables (scripts SQL fournis dans /sql/)
```

### Lancement de l'Application
```bash
# Compilation
javac -cp "lib/*" src/**/*.java

# Exécution
java -cp "lib/*:src" com.banking.BankingApplication
```

## 🎮 Interface Console

### Menu Principal (Non connecté)
```
1️⃣ Login → Se connecter
2️⃣ Exit → Quitter
```

### Menu Principal (Connecté)
```
👤 Logged in as [Nom]

🏦 Comptes
   - Créer un compte
   - Lister mes comptes
   - Mettre à jour profil
   - Changer mot de passe
   - Clôturer compte

💰 Transactions
   - Effectuer un dépôt
   - Effectuer un retrait
   - Effectuer un virement
   - Consulter l'historique

💸 Frais
   - Lister les règles
   - Ajouter/Modifier règle
   - Activer/Désactiver règle

💳 Crédits
   - Demander un crédit
   - Suivi des crédits
   - Remboursement

📊 Rapports
   - Consulter statistiques
   - Exporter rapport
   - Revenus des frais

🔐 Compte
   - Se déconnecter
   - Quitter
```

## 📐 Règles Métier

### Validations Essentielles
- ✅ Montants strictement positifs (2 décimales)
- ✅ Découvert autorisé uniquement si configuré
- ✅ Clôture refusée si crédit actif ou en retard
- ✅ Crédit accepté uniquement si revenu suffisant
- ✅ Historisation obligatoire de toutes les opérations
- ✅ Conversion automatique multi-devises
- ✅ Détection et blocage des transactions suspectes (AML)

## 📁 Structure du Projet

```
src/

├── entities/          # Entités métier
├── enums/          # enumeration
├── helpers/          # validation
├── repository/      # Accès aux données
├── service/         # Logique métier
├── controller/      # Contrôleurs
├── views/             # Interface console
├── config/         # Configuration et gestion .env
├── handlers/       # Gestionnaires d'erreurs et événements
├── helpers/        # Classes utilitaires et méthodes d'aide
├── exception/      # Exceptions personnalisées métier
├── sql/
│   └── schema.sql      # Scripts de création BD
├── .env                # Variables d'environnement (à créer)
├── .env.example        # Exemple de configuration
└── .gitignore          # Fichiers à ignorer
```

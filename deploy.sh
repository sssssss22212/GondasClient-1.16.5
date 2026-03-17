#!/bin/bash

# ============================================
# GondasClient Auto-Deploy Script for Termux
# Автоматическое создание репо и загрузка на GitHub
# ============================================

set -e

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m'

echo -e "${PURPLE}"
echo "============================================"
echo "  GondasClient Auto-Deploy Script"
echo "  For Termux / Linux"
echo "============================================"
echo -e "${NC}"

# Проверка Termux
if command -v pkg &> /dev/null; then
    IS_TERMUX=true
    echo -e "${CYAN}[*] Detected Termux environment${NC}"
else
    IS_TERMUX=false
    echo -e "${CYAN}[*] Standard Linux environment${NC}"
fi

# Установка зависимостей
install_deps() {
    echo -e "${YELLOW}[*] Installing dependencies...${NC}"
    
    if [ "$IS_TERMUX" = true ]; then
        pkg update -y
        pkg install -y git curl jq zip unzip
    else
        if command -v apt-get &> /dev/null; then
            sudo apt-get update
            sudo apt-get install -y git curl jq zip unzip
        elif command -v yum &> /dev/null; then
            sudo yum install -y git curl jq zip unzip
        elif command -v pacman &> /dev/null; then
            sudo pacman -S --noconfirm git curl jq zip unzip
        fi
    fi
    
    echo -e "${GREEN}[✓] Dependencies installed${NC}"
}

# Проверка git
if ! command -v git &> /dev/null; then
    install_deps
fi

# Конфигурация
REPO_NAME="GondasClient-1.16.5"
WORK_DIR="/tmp/gondasclient-deploy"

echo ""
echo -e "${CYAN}GitHub Authentication Options:${NC}"
echo "  1) Personal Access Token (recommended)"
echo "  2) Device Code Login (browser redirect)"
echo ""
read -p "Select auth method [1/2]: " AUTH_METHOD

# Создаём рабочую директорию
rm -rf "$WORK_DIR"
mkdir -p "$WORK_DIR"

# Копируем все файлы проекта
echo -e "${YELLOW}[*] Preparing project files...${NC}"
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

# Если скрипт запущен из папки с проектом
if [ -f "$SCRIPT_DIR/build.gradle" ]; then
    PROJECT_DIR="$SCRIPT_DIR"
else
    # Иначе ищем в текущей директории или запрашиваем путь
    if [ -f "./build.gradle" ] && [ -d "./src/main/java/com/gondas" ]; then
        PROJECT_DIR="$(pwd)"
    else
        echo -e "${YELLOW}[*] Project files not found in current directory${NC}"
        echo -e "${YELLOW}[*] Downloading from GitHub...${NC}"
        # Скачиваем исходники
        curl -L "https://github.com/sssssss22212/SSSS/archive/refs/tags/Pl.zip" -o /tmp/source.zip
        unzip -o /tmp/source.zip -d /tmp/source_extract
        
        # Копируем улучшенные файлы из текущей директории скрипта
        PROJECT_DIR="$WORK_DIR/project"
        mkdir -p "$PROJECT_DIR"
        cp -r "$SCRIPT_DIR"/* "$PROJECT_DIR/" 2>/dev/null || true
    fi
fi

# Копируем проект в рабочую директорию
cp -r "$PROJECT_DIR"/* "$WORK_DIR/" 2>/dev/null || true
cd "$WORK_DIR"

# Убеждаемся что все нужные файлы есть
if [ ! -f "build.gradle" ]; then
    echo -e "${RED}[!] build.gradle not found! Creating...${NC}"
    cat > build.gradle << 'BUILDCONFIG'
buildscript {
    repositories {
        maven { url = 'https://maven.minecraftforge.net' }
        mavenCentral()
    }
    dependencies {
        classpath group: 'net.minecraftforge.gradle', name: 'ForgeGradle', version: '5.1.+', changing: true
    }
}

apply plugin: 'net.minecraftforge.gradle'
apply plugin: 'eclipse'
apply plugin: 'maven-publish'

version = '2.0.0'
group = 'com.gondas.client'
archivesBaseName = 'GondasClient-1.16.5'

java.toolchain.languageVersion = JavaLanguageVersion.of(8)

minecraft {
    mappings channel: 'official', version: '1.16.5'
    runs {
        client {
            workingDirectory project.file('run')
            property 'forge.logging.markers', 'REGISTRIES'
            property 'forge.logging.console.level', 'debug'
            mods { gondasclient { source sourceSets.main } }
        }
    }
}

dependencies {
    minecraft 'net.minecraftforge:forge:1.16.5-36.2.39'
}

jar {
    manifest {
        attributes([
            "Specification-Title": "GondasClient",
            "Specification-Vendor": "Gondas",
            "Specification-Version": "2.0",
            "Implementation-Title": project.name,
            "Implementation-Version": "${version}",
            "Implementation-Vendor": "Gondas"
        ])
    }
}

jar.finalizedBy('reobfJar')
tasks.withType(JavaCompile).configureEach { options.encoding = 'UTF-8' }
BUILDCONFIG
fi

# Создаём .gitignore
cat > .gitignore << 'GITIGNORE'
.gradle/
build/
.idea/
*.iml
run/
*.log
.DS_Store
Thumbs.db
GITIGNORE

# Создаём README если нет
if [ ! -f "README.md" ]; then
    cat > README.md << 'README'
# GondasClient v2.0.0

Minecraft 1.16.5 Forge Client Mod with mobile launcher support.

## Features
- 60+ modules for combat, movement, render, player, world
- PojavLauncher / MCLauncher / Fold Craft Launcher support
- Touch-friendly ClickGUI
- Anticheat bypass (NCP, AAC, Matrix, Grim)

## Installation
Download the JAR from [Releases](../../releases) and put it in your mods folder.

## Controls
- RIGHT SHIFT - Open ClickGUI
- LMB - Toggle module
- RMB - Open settings

## Build Status
![Build](../../actions/workflows/build.yml/badge.svg)
README
fi

# Создаём GitHub Actions workflow
mkdir -p .github/workflows
cat > .github/workflows/build.yml << 'WORKFLOW'
name: Build GondasClient

on:
  push:
    branches: [ main, master ]
  pull_request:
    branches: [ main, master ]
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v4
    
    - name: Set up JDK 8
      uses: actions/setup-java@v4
      with:
        java-version: '8'
        distribution: 'temurin'

    - name: Setup Gradle
      uses: gradle/actions/setup-gradle@v3

    - name: Build with Gradle
      run: chmod +x gradlew && ./gradlew build --no-daemon

    - name: Upload artifact
      uses: actions/upload-artifact@v4
      with:
        name: GondasClient-1.16.5
        path: build/libs/*.jar

    - name: Create Release
      if: github.event_name == 'push' && startsWith(github.ref, 'refs/tags/')
      uses: softprops/action-gh-release@v1
      with:
        files: build/libs/*.jar
      env:
        GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
WORKFLOW

echo -e "${GREEN}[✓] Project files prepared${NC}"

# Авторизация в GitHub
authenticate_github() {
    if [ "$AUTH_METHOD" = "1" ]; then
        # Token метод
        echo ""
        echo -e "${CYAN}Create a Personal Access Token:${NC}"
        echo "  1. Go to: https://github.com/settings/tokens/new"
        echo "  2. Select scopes: repo, workflow"
        echo "  3. Generate token and copy it"
        echo ""
        read -p "Enter your GitHub token: " TOKEN
        
        # Получаем имя пользователя
        USER_RESPONSE=$(curl -s -H "Authorization: token $TOKEN" https://api.github.com/user)
        GITHUB_USER=$(echo "$USER_RESPONSE" | jq -r '.login')
        
        if [ "$GITHUB_USER" = "null" ] || [ -z "$GITHUB_USER" ]; then
            echo -e "${RED}[!] Invalid token${NC}"
            exit 1
        fi
        
        echo -e "${GREEN}[✓] Authenticated as: $GITHUB_USER${NC}"
        
    else
        # Device code method
        echo ""
        echo -e "${CYAN}[*] Starting device code authentication...${NC}"
        
        # Регистрируем устройство
        DEVICE_RESPONSE=$(curl -s -X POST \
            -H "Accept: application/json" \
            -H "Content-Type: application/json" \
            -d '{"client_id":"Iv1.b507a08c87ecfe68","scopes":["repo","workflow"]}' \
            https://github.com/login/device/code)
        
        DEVICE_CODE=$(echo "$DEVICE_RESPONSE" | jq -r '.device_code')
        USER_CODE=$(echo "$DEVICE_RESPONSE" | jq -r '.user_code')
        VERIFICATION_URI=$(echo "$DEVICE_RESPONSE" | jq -r '.verification_uri')
        INTERVAL=$(echo "$DEVICE_RESPONSE" | jq -r '.interval // 5')
        
        echo ""
        echo -e "${GREEN}========================================${NC}"
        echo -e "${YELLOW}  AUTHENTICATION REQUIRED${NC}"
        echo -e "${GREEN}========================================${NC}"
        echo ""
        echo -e "  Code: ${CYAN}$USER_CODE${NC}"
        echo ""
        echo -e "  URL:  ${CYAN}$VERIFICATION_URI${NC}"
        echo ""
        echo -e "${GREEN}========================================${NC}"
        echo ""
        
        # Пробуем открыть браузер
        if command -v termux-open-url &> /dev/null; then
            termux-open-url "$VERIFICATION_URI" 2>/dev/null &
        elif command -v xdg-open &> /dev/null; then
            xdg-open "$VERIFICATION_URI" 2>/dev/null &
        elif command -v open &> /dev/null; then
            open "$VERIFICATION_URI" 2>/dev/null &
        fi
        
        echo -e "${YELLOW}[*] Waiting for authentication...${NC}"
        echo -e "${YELLOW}[*] Enter the code above in your browser${NC}"
        
        # Ждём авторизации
        while true; do
            sleep "$INTERVAL"
            
            TOKEN_RESPONSE=$(curl -s -X POST \
                -H "Accept: application/json" \
                -H "Content-Type: application/json" \
                -d "{\"client_id\":\"Iv1.b507a08c87ecfe68\",\"device_code\":\"$DEVICE_CODE\",\"grant_type\":\"urn:ietf:params:oauth:grant-type:device_code\"}" \
                https://github.com/login/oauth/access_token)
            
            TOKEN=$(echo "$TOKEN_RESPONSE" | jq -r '.access_token // empty')
            
            if [ -n "$TOKEN" ]; then
                echo ""
                echo -e "${GREEN}[✓] Authentication successful!${NC}"
                break
            fi
            
            ERROR=$(echo "$TOKEN_RESPONSE" | jq -r '.error // empty')
            if [ "$ERROR" = "authorization_pending" ]; then
                echo -ne "${YELLOW}.${NC}"
                continue
            elif [ "$ERROR" = "expired_token" ]; then
                echo -e "${RED}[!] Code expired. Please restart.${NC}"
                exit 1
            fi
        done
        
        # Получаем имя пользователя
        USER_RESPONSE=$(curl -s -H "Authorization: token $TOKEN" https://api.github.com/user)
        GITHUB_USER=$(echo "$USER_RESPONSE" | jq -r '.login')
        
        echo -e "${GREEN}[✓] Logged in as: $GITHUB_USER${NC}"
    fi
    
    export GITHUB_TOKEN="$TOKEN"
    export GITHUB_USER="$GITHUB_USER"
}

authenticate_github

# Проверяем существует ли репозиторий
echo ""
echo -e "${YELLOW}[*] Checking repository...${NC}"

REPO_CHECK=$(curl -s -o /dev/null -w "%{http_code}" \
    -H "Authorization: token $GITHUB_TOKEN" \
    "https://api.github.com/repos/$GITHUB_USER/$REPO_NAME")

if [ "$REPO_CHECK" = "200" ]; then
    echo -e "${YELLOW}[*] Repository exists, updating...${NC}"
else
    echo -e "${YELLOW}[*] Creating new repository...${NC}"
    
    curl -s -X POST \
        -H "Authorization: token $GITHUB_TOKEN" \
        -H "Accept: application/vnd.github.v3+json" \
        -d "{\"name\":\"$REPO_NAME\",\"description\":\"GondasClient - Minecraft 1.16.5 Forge Mod with mobile support\",\"private\":false,\"auto_init\":false}" \
        https://api.github.com/user/repos > /dev/null
    
    echo -e "${GREEN}[✓] Repository created${NC}"
fi

# Инициализация git
echo -e "${YELLOW}[*] Initializing git...${NC}"

git init
git config user.email "$GITHUB_USER@users.noreply.github.com"
git config user.name "$GITHUB_USER"

# Добавляем все файлы
git add -A
git commit -m "GondasClient v2.0.0 - Mobile optimized

Features:
- 60+ modules (Combat, Movement, Render, Player, World, Misc)
- PojavLauncher / MCLauncher / Fold Craft Launcher support
- Touch-friendly ClickGUI with adaptive scaling
- Anticheat bypass (NCP, AAC, Matrix, Grim)
- Performance optimization for mobile devices
- Auto-detection of mobile launcher environment

Controls:
- RIGHT SHIFT - Open ClickGUI
- LMB - Toggle module
- RMB - Open settings"

# Устанавливаем remote и пушим
git branch -M main
git remote add origin "https://$GITHUB_TOKEN@github.com/$GITHUB_USER/$REPO_NAME.git" 2>/dev/null || \
git remote set-url origin "https://$GITHUB_TOKEN@github.com/$GITHUB_USER/$REPO_NAME.git"

echo -e "${YELLOW}[*] Pushing to GitHub...${NC}"
git push -u origin main --force

echo ""
echo -e "${GREEN}============================================${NC}"
echo -e "${GREEN}  SUCCESS! Repository created!${NC}"
echo -e "${GREEN}============================================${NC}"
echo ""
echo -e "  Repository: ${CYAN}https://github.com/$GITHUB_USER/$REPO_NAME${NC}"
echo ""
echo -e "  Build Status: ${CYAN}https://github.com/$GITHUB_USER/$REPO_NAME/actions${NC}"
echo ""
echo -e "  Download JAR: ${CYAN}https://github.com/$GITHUB_USER/$REPO_NAME/releases${NC}"
echo ""
echo -e "${YELLOW}[*] GitHub Actions is building your mod...${NC}"
echo -e "${YELLOW}[*] Check Actions tab for build progress${NC}"
echo ""
echo -e "${GREEN}============================================${NC}"
echo ""
echo -e "Press Enter to open repository in browser..."
read

# Открываем репозиторий
if command -v termux-open-url &> /dev/null; then
    termux-open-url "https://github.com/$GITHUB_USER/$REPO_NAME/actions"
elif command -v xdg-open &> /dev/null; then
    xdg-open "https://github.com/$GITHUB_USER/$REPO_NAME/actions"
fi

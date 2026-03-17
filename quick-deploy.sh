#!/bin/bash

# ============================================
# GONADS CLIENT - ONE-CLICK DEPLOY SCRIPT
# For Termux / Linux / macOS
# ============================================

clear
echo -e "\033[1;35m"
echo "╔══════════════════════════════════════════╗"
echo "║     GONDA S CLIENT AUTO-DEPLOYER        ║"
echo "║         GitHub + Actions Build          ║"
echo "╚══════════════════════════════════════════╝"
echo -e "\033[0m"

# Установка зависимостей для Termux
if command -v pkg &> /dev/null; then
    echo -e "\033[1;33m[*] Installing Termux dependencies...\033[0m"
    pkg update -y 2>/dev/null
    pkg install -y git curl jq zip 2>/dev/null
fi

# Проверка зависимостей
for cmd in git curl jq; do
    if ! command -v $cmd &> /dev/null; then
        echo -e "\033[1;31m[!] $cmd not found. Please install: apt install $cmd\033[0m"
        exit 1
    fi
done

echo ""
echo -e "\033[1;36mGitHub Login Method:\033[0m"
echo "  1. Personal Access Token (fast)"
echo "  2. Browser Login (easy)"
echo ""
read -p "Select [1/2]: " choice

if [ "$choice" = "1" ]; then
    echo ""
    echo -e "\033[1;33mCreate token: https://github.com/settings/tokens/new\033[0m"
    echo "Select scopes: repo, workflow"
    echo ""
    read -p "Paste token: " TOKEN
else
    echo ""
    echo -e "\033[1;33m[*] Getting login code...\033[0m"
    
    # Device flow
    RESP=$(curl -s -X POST \
        -H "Accept: application/json" \
        -d '{"client_id":"Iv1.b507a08c87ecfe68","scopes":["repo","workflow"]}' \
        https://github.com/login/device/code)
    
    CODE=$(echo "$RESP" | jq -r '.user_code')
    URL=$(echo "$RESP" | jq -r '.verification_uri')
    DEVICE=$(echo "$RESP" | jq -r '.device_code')
    
    echo ""
    echo -e "\033[1;32m╔══════════════════════════════════════════╗"
    echo -e "║    ENTER THIS CODE IN YOUR BROWSER:     ║"
    echo -e "╠══════════════════════════════════════════╣"
    echo -e "║                                          ║"
    echo -e "║   CODE: \033[1;36m$CODE\033[0m\033[1;32m                      ║"
    echo -e "║                                          ║"
    echo -e "╚══════════════════════════════════════════╝\033[0m"
    echo ""
    echo -e "URL: \033[1;36m$URL\033[0m"
    echo ""
    
    # Открываем браузер
    if command -v termux-open-url &> /dev/null; then
        termux-open-url "$URL" &
    elif command -v xdg-open &> /dev/null; then
        xdg-open "$URL" &
    fi
    
    echo -e "\033[1;33m[*] Waiting for you to authorize...\033[0m"
    
    # Ждём токен
    while true; do
        sleep 5
        TRESP=$(curl -s -X POST \
            -H "Accept: application/json" \
            -d "{\"client_id\":\"Iv1.b507a08c87ecfe68\",\"device_code\":\"$DEVICE\",\"grant_type\":\"urn:ietf:params:oauth:grant-type:device_code\"}" \
            https://github.com/login/oauth/access_token)
        
        TOKEN=$(echo "$TRESP" | jq -r '.access_token // empty')
        
        if [ -n "$TOKEN" ]; then
            echo -e "\033[1;32m[✓] Logged in!\033[0m"
            break
        fi
        
        echo -ne "\033[1;33m.\033[0m"
    done
fi

# Получаем юзернейм
USER=$(curl -s -H "Authorization: token $TOKEN" https://api.github.com/user | jq -r '.login')
echo -e "\033[1;32m[✓] Hello, $USER!\033[0m"

# Создаём репозиторий
REPO="GondasClient-1.16.5"
echo -e "\033[1;33m[*] Creating repository: $REPO\033[0m"

curl -s -X POST \
    -H "Authorization: token $TOKEN" \
    -H "Accept: application/vnd.github.v3+json" \
    -d "{\"name\":\"$REPO\",\"description\":\"Minecraft 1.16.5 Forge Mod - Mobile Optimized\",\"private\":false}" \
    https://api.github.com/user/repos > /dev/null 2>&1

# Подготовка файлов
echo -e "\033[1;33m[*] Preparing files...\033[0m"
DIR="/tmp/gondasclient-$$"
mkdir -p "$DIR"

# Определяем папку с проектом
if [ -f "build.gradle" ]; then
    PROJECT_DIR="."
else
    PROJECT_DIR="$(dirname "$0")"
    if [ ! -f "$PROJECT_DIR/build.gradle" ]; then
        PROJECT_DIR="/home/z/my-project/minecraft_mod/gondasclient"
    fi
fi

# Копируем файлы
cp -r "$PROJECT_DIR"/* "$DIR/" 2>/dev/null
cd "$DIR"

# Создаём .gitignore
echo ".gradle/
build/
.idea/
*.iml
run/
*.log" > .gitignore

# Создаём workflow
mkdir -p .github/workflows
cat > .github/workflows/build.yml << 'EOF'
name: Build

on:
  push:
    branches: [main]
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v4
    
    - uses: actions/setup-java@v4
      with:
        java-version: '8'
        distribution: 'temurin'
    
    uses: gradle/actions/setup-gradle@v3
    
    - run: chmod +x gradlew && ./gradlew build --no-daemon
    
    - uses: actions/upload-artifact@v4
      with:
        name: GondasClient-JAR
        path: build/libs/*.jar
EOF

# Git
git init
git config user.email "$USER@users.noreply.github.com"
git config user.name "$USER"
git add -A
git commit -m "GondasClient v2.0.0 - Mobile Optimized

Features:
- 60+ modules for Minecraft 1.16.5
- PojavLauncher / MCLauncher support
- Touch-friendly ClickGUI
- Anticheat bypass

Press RIGHT SHIFT to open ClickGUI"

git branch -M main
git remote add origin "https://$TOKEN@github.com/$USER/$REPO.git"

echo -e "\033[1;33m[*] Uploading to GitHub...\033[0m"
git push -u origin main --force

echo ""
echo -e "\033[1;32m╔══════════════════════════════════════════╗"
echo -e "║           SUCCESS! 🎉                   ║"
echo -e "╠══════════════════════════════════════════╣"
echo -e "║                                          ║"
echo -e "║  Repository:                            ║"
echo -e "║  \033[1;36mhttps://github.com/$USER/$REPO\033[0m\033[1;32m"
echo -e "║                                          ║"
echo -e "║  Build Progress:                        ║"
echo -e "║  \033[1;36mhttps://github.com/$USER/$REPO/actions\033[0m\033[1;32m"
echo -e "║                                          ║"
echo -e "║  Download JAR (after build completes):  ║"
echo -e "║  \033[1;36mhttps://github.com/$USER/$REPO/actions\033[0m\033[1;32m"
echo -e "║                                          ║"
echo -e "╚══════════════════════════════════════════╝\033[0m"
echo ""

# Открываем Actions
if command -v termux-open-url &> /dev/null; then
    termux-open-url "https://github.com/$USER/$REPO/actions"
fi

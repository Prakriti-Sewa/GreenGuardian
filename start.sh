#!/bin/bash

# GreenGuardian - Start Script
# This script helps you start the backend server and frontend application

echo "🌿 GreenGuardian - Environmental Issue Reporting Platform"
echo "=========================================================="
echo ""

# Check if we're in the right directory
if [ ! -f "settings.gradle.kts" ]; then
    echo "❌ Error: Please run this script from the project root directory"
    exit 1
fi

# Function to start backend
start_backend() {
    echo "🚀 Starting Backend Server..."
    echo "   Port: 8080"
    echo "   Database: H2 (in-memory)"
    echo ""
    ./gradlew :server:run &
    BACKEND_PID=$!
    echo "   Backend PID: $BACKEND_PID"
    echo ""
}

# Function to start desktop frontend
start_desktop() {
    echo "🖥️  Starting Desktop Application..."
    echo ""
    ./gradlew :composeApp:run &
    DESKTOP_PID=$!
    echo "   Desktop PID: $DESKTOP_PID"
    echo ""
}

# Function to start web frontend
start_web() {
    echo "🌐 Starting Web Application..."
    echo "   URL: http://localhost:8080"
    echo ""
    ./gradlew :composeApp:jsBrowserDevelopmentRun
}

# Function to show menu
show_menu() {
    echo "What would you like to start?"
    echo ""
    echo "1) Backend Server Only"
    echo "2) Desktop Application Only (requires backend)"
    echo "3) Backend + Desktop Application"
    echo "4) Web Application (requires backend)"
    echo "5) Backend + Web Application"
    echo "6) Exit"
    echo ""
    read -p "Enter your choice (1-6): " choice
    
    case $choice in
        1)
            start_backend
            echo "✅ Backend server started!"
            echo "   API available at: http://localhost:8080/api"
            echo ""
            echo "Press Ctrl+C to stop the server"
            wait $BACKEND_PID
            ;;
        2)
            start_desktop
            echo "✅ Desktop application started!"
            echo ""
            echo "Press Ctrl+C to stop the application"
            wait $DESKTOP_PID
            ;;
        3)
            start_backend
            sleep 3  # Wait for backend to start
            start_desktop
            echo "✅ Both backend and desktop started!"
            echo ""
            echo "Press Ctrl+C to stop all services"
            wait
            ;;
        4)
            start_web
            ;;
        5)
            start_backend
            sleep 3  # Wait for backend to start
            start_web
            ;;
        6)
            echo "👋 Goodbye!"
            exit 0
            ;;
        *)
            echo "❌ Invalid choice. Please try again."
            echo ""
            show_menu
            ;;
    esac
}

# Cleanup function
cleanup() {
    echo ""
    echo "🛑 Stopping all services..."
    if [ ! -z "$BACKEND_PID" ]; then
        kill $BACKEND_PID 2>/dev/null
        echo "   Stopped backend (PID: $BACKEND_PID)"
    fi
    if [ ! -z "$DESKTOP_PID" ]; then
        kill $DESKTOP_PID 2>/dev/null
        echo "   Stopped desktop (PID: $DESKTOP_PID)"
    fi
    echo "✅ All services stopped"
    exit 0
}

# Set up trap to catch Ctrl+C
trap cleanup INT

# Show menu
show_menu

#!/bin/bash

echo "🚀 Iniciando Registrar Laboreo en Lotes - Versión con Persistencia"
echo "=================================================="
echo ""
echo "📦 Compilando con Maven..."
mvn clean install -DskipTests

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Compilación exitosa!"
    echo ""
    echo "🏃 Ejecutando aplicación..."
    echo ""
    echo "📍 La aplicación estará disponible en:"
    echo "   - API: http://localhost:8080"
    echo "   - H2 Console: http://localhost:8080/h2-console"
    echo ""
    echo "🔑 Credenciales H2 Console:"
    echo "   - JDBC URL: jdbc:h2:mem:laboreosdb"
    echo "   - Username: sa"
    echo "   - Password: (vacío)"
    echo ""
    echo "=================================================="
    echo ""
    
    mvn spring-boot:run
else
    echo ""
    echo "❌ Error en la compilación"
    exit 1
fi


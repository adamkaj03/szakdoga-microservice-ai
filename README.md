# Könyváruház Webalkalmazás – Mikroszervizes Architektúra

Ez a projekt egy informatikai könyveket árusító webshop, amely modern webes technológiákra, JWT tokenes autentikációra, valamint mikroszervizes architektúrára épül. A rendszer célja, hogy a vásárlók és adminisztrátorok számára skálázható, bővíthető és biztonságos platformot biztosítson, valamint a legújabb mesterséges intelligencia-alapú modulokat is integrálja.

## Fő funkciók

- Könyvek keresése, kategória szerinti szűrés
- Könyvek részletes adatlapjának megtekintése
- Kosárba helyezés, rendelés leadása
- JWT-alapú regisztráció, bejelentkezés, kijelentkezés
- Szerepkörök: USER és ADMIN
    - USER: rendelés leadása
    - ADMIN: rendelések listázása, új kategória és könyv hozzáadása
- Képek feltöltése és tárolása Azure Blob Storage-ban

## AI-alapú bővítések (terv/tartalom)

- **Ajánlórendszer:** Személyre szabott termékajánlások generálása a felhasználói viselkedés és vásárlási előzmények alapján.
- **Automatikus leírásgenerálás:** Adminisztrátori feltöltés során, képből rövid termékleírás generálása AI segítségével.
- **AI-támogatott keresés:** Természetes nyelvi keresés és kép alapú keresés.
- **Dinamikus árképzés:** Algoritmus, amely figyelembe veszi a keresletet, kategóriát, felhasználói szegmenseket.
- **Felhasználói szegmentáció:** Vásárlói adatok alapján célzott ajánlatok, hirdetések.

## Architektúra

A rendszer mikroszervizes architektúrában épül fel, minden fő modul különálló konténerben fut.

### Fő komponensek

- **Frontend**: Angular alkalmazás (`frontend/`)
- **Backend**: Java Spring Boot REST API (`backend/`)
- **Adatbázis**: MySQL (hivatalos Docker image)
- **(Terv) Email service**: Mikroszerviz email küldéshez
- **(Terv) AI-szervizek**: Python alapú AI mikroszervizek (ajánlórendszer, leírásgenerálás, stb.)

A komponensek közötti kapcsolat és adattovábbítás HTTP REST API-kon keresztül történik. A képek feltöltése és kiszolgálása Azure Blob Storage-on keresztül valósul meg.

## Fejlesztői környezet (Docker Compose)

A teljes rendszer konténerizált, fejlesztéshez és teszteléshez `docker-compose` használható.

### Fő parancsok

1. **Projekt klónozása**
   ```bash
   git clone https://github.com/adamkaj03/halado_adatb.git
   cd halado_adatb
   ```

2. **Docker Compose indítása**
   ```bash
   docker-compose up --build
   ```

3. **Elérési pontok**
   - Frontend: [http://localhost:4200](http://localhost:4200)
   - Backend API: [http://localhost:8080](http://localhost:8080)
   - MySQL: `localhost:3306` (alapértelmezett felhasználó/jelszó: lásd `docker-compose.yml`)

### Környezeti változók

A backend és egyes szervizek működéséhez szükséges környezeti változók a `docker-compose.yml`-ben állíthatók be (pl. adatbázis elérés, Azure Storage kulcsok).

## Mappa szerkezet

```
.
├── backend/           # Java Spring Boot REST API
├── frontend/          # Angular alkalmazás
├── email-service/     # (Terv) Email küldő mikroszerviz
├── ai-recommender/    # (Terv) Ajánlórendszer mikroszerviz
├── docker-compose.yml # Konténer orchestration
└── README.md
```

## Biztonság és skálázhatóság

- JWT tokenes autentikáció és szerepkörkezelés
- REST API endpointok jogosultság-alapú védelme
- Minden új AI-modul és service külön konténerben, könnyen skálázhatóan deployolható

## Hozzájárulás

Szívesen fogadunk pull requesteket, javaslatokat! Kérjük, kövesd a meglévő kódstílust és a mikroszervizes elveket.

## Licenc

MIT

---

**A projekt jelenleg fejlesztés alatt áll, a mikroszerviz architektúra kiépítése és AI-modulok integrációja folyamatban!**

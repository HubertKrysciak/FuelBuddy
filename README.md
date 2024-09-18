# **FuelBuddy - Dokument Wymagań**
## Cel Aplikacji
Stworzenie aplikacji FuelBuddy do zapisywania tankowań samochodu, umożliwiającej analizę zużycia paliwa i kosztów eksploatacji. Aplikacja pozwoli użytkownikom na śledzenie swoich tankowań, analizowanie danych oraz wizualizację informacji w postaci wykresów.

### Technologie
- Język programowania: Java
- Framework: Spring Boot
- Silnik szablonów: Thymeleaf
- Baza danych: PostgreSQL
- Architektura: Backend i frontend w jednej aplikacji
- Inne biblioteki:
- Spring Web
- Spring Data JPA
- Spring Security
- Validation (JSR-303)
- Lombok
- Biblioteki JavaScript do wykresów (np. Chart.js, Highcharts)
- Spring Boot DevTools

### Funkcjonalności
#### Użytkownicy i Bezpieczeństwo
- Uwierzytelnianie użytkowników:
- Rejestracja i logowanie użytkowników
- Bezpieczne przechowywanie haseł (np. hashowanie z BCrypt)
- Role użytkowników:
- Użytkownik standardowy:
    - Może zarządzać swoimi tankowaniami
    - Ma dostęp tylko do swoich danych
    - Po zalogowaniu wybiera, dla którego samochodu wprowadza dane
    - Może dodawać i zarządzać wieloma samochodami
- Administrator:
- Ma te same możliwości co użytkownik standardowy
- Może zarządzać kontami użytkowników (dodawanie, edycja, usuwanie)
- Ma możliwość podglądu i edycji danych wszystkich użytkowników
- Autoryzacja dostępu:
- Kontrola dostępu na podstawie ról i uprawnień
- Ochrona przed nieautoryzowanym dostępem do danych
- Walidacja danych:
- Sprawdzanie poprawności wprowadzanych danych
- Ochrona przed atakami typu SQL Injection, XSS itp.
- Logowanie aktywności:
- Rejestrowanie istotnych zdarzeń w systemie (np. próby logowania, edycje danych)

#### Zarządzanie Pojazdami
- Użytkownik może dodawać i zarządzać wieloma pojazdami
- Przy wprowadzaniu tankowania użytkownik wybiera pojazd, którego dotyczą dane
- Informacje o pojeździe:
- Marka
- Model
- Rok produkcji
- Numer rejestracyjny (opcjonalnie)
- Notatki dodatkowe (opcjonalnie)

#### Zarządzanie Tankowaniami
- Dane do zbierania przy każdym tankowaniu:
- Data tankowania
- Przebieg pojazdu w momencie tankowania
- Ilość paliwa zatankowanego (w litrach)
- Cena jednostkowa paliwa (za litr)
- Otrzymany rabat (kwotowo lub procentowo)
- Rodzaj paliwa (benzyna, diesel, LPG itp.)
- Stacja paliw (nazwa stacji)
- Notatki dodatkowe (opcjonalnie)
- Możliwość edycji i usuwania wprowadzonych tankowań
- Lista tankowań z paginacją (50 wpisów na stronę)

#### Analityka i Wizualizacje
- Funkcjonalności analityczne:
- Obliczanie średniego zużycia paliwa na 100 km
- Śledzenie całkowitych kosztów paliwa w określonym przedziale czasowym
- Analiza średniej ceny paliwa w czasie
- Obliczanie kosztu na kilometr
- Ocena oszczędności uzyskanych dzięki rabatom
- Porównanie stacji paliw pod kątem opłacalności
- Monitorowanie trendów zużycia paliwa
- Wizualizacje danych:
- Wykres liniowy zmian średniego zużycia paliwa w czasie
- Wykres słupkowy kosztów tankowania na różnych stacjach paliw
- Wykres kołowy udziału wydatków na paliwo w poszczególnych miesiącach
- Wykres punktowy zależności między przebiegiem a ilością zużytego paliwa
- Histogram cen jednostkowych paliwa
- Wykres trendu zmian cen paliwa lub zużycia w dłuższym okresie

## Widoki Aplikacji
### Strona Logowania
- Pola:
- Login
- Hasło
- Przyciski:
- Zaloguj się — logowanie użytkownika
- Rejestracja — przekierowanie do formularza rejestracji
- Link:
- "Nie masz konta? Zarejestruj się."

### Formularz Rejestracji
- Pola:
- Login
- E-mail (opcjonalnie)
- Hasło
- Potwierdź hasło
- Przycisk:
- Zarejestruj się

### Widok Użytkownika (po zalogowaniu)
- Górna część strony:
  - Lista rozwijana do wyboru pojazdu
    - Obok przycisk "Dodaj pojazd"
- Formularz wprowadzania danych tankowania:
  - Data tankowania
  - Przebieg pojazdu
  - Ilość paliwa
  - Cena jednostkowa paliwa
  - Otrzymany rabat
  - Rodzaj paliwa
  - Stacja paliw
  - Notatki dodatkowe
- Lewa strona:
  - Lista tankowań dla wybranego pojazdu
    - Paginacja: 50 wpisów na stronę
- Prawa strona:
   - Analityka i wykresy:
     - Wyświetlanie wykresów i statystyk zgodnie z sekcją Analityka i Wizualizacje
- Prawy górny róg:
    - Ustawienia:
        - Zmień hasło
    - Wyloguj się
### Widok Administratora
  - Zawiera wszystkie elementy widoku użytkownika
  - Dodatkowe opcje:
    - Zarządzanie użytkownikami:
      - Lista wszystkich użytkowników
        - Obok każdego użytkownika przycisk "Przejdź do widoku użytkownika"
      - Możliwość dodawania, edycji i usuwania użytkowników
### Widok Dodawania/Edycji Pojazdu
  - Pola:
    - Marka
    - Model
    - Rok produkcji
    - Numer rejestracyjny (opcjonalnie)
    - Notatki dodatkowe (opcjonalnie)
  - Przyciski:
    - Zapisz
    - Anuluj
### Widok Zmiany Hasła
  - Pola:
    - Aktualne hasło
    - Nowe hasło
    - Potwierdź nowe hasło
  - Przycisk:
    - Zmień hasło
  
##   Szczegóły Techniczne
###   Baza Danych

- Encje:
  - **`User`**
    - id: Long
    - username: String
    - password: String
    - role: String (ROLE_USER, ROLE_ADMIN)
    - **Relacje:**
      - vehicles: Lista pojazdów (OneToMany z Vehicle)
  - **`Vehicle`**
    - id: Long
    - make: String (marka)
    - model: String
    - year: Integer
    - registrationNumber: String (opcjonalnie)
    - **Relacje:**
      - user: Właściciel (ManyToOne z User)
      - fuelEntries: Lista tankowań (OneToMany z FuelEntry)
  - **`FuelEntry`**
    - id: Long
    - date: LocalDate
    - mileage: Integer
    - fuelVolume: Double
    - pricePerUnit: Double
    - discount: Double
    - fuelType: String
    - gasStation: String
    - notes: String (opcjonalnie)
    - **Relacje:**
      - vehicle: Pojazd (ManyToOne z Vehicle)

### Relacje między encjami

- User ma wiele Vehicle (`OneToMany`)
- Vehicle należy do User (`ManyToOne`)
- Vehicle ma wiele FuelEntry (`OneToMany`)
- FuelEntry należy do Vehicle (`ManyToOne`)

### Bezpieczeństwo

- Hashowanie haseł: `BCryptPasswordEncoder`
- Spring Security:
  - Konfiguracja dostępu do URL-i na podstawie ról
  - Formularze logowania i rejestracji
  - Zabezpieczenie przed nieautoryzowanym dostępem


## Uwagi Dodatkowe

* Responsywność interfejsu: Dostosowanie do urządzeń mobilnych i różnych rozdzielczości ekranów
* Spójny design: Ujednolicona kolorystyka i styl interfejsu
* Walidacja danych po stronie klienta i serwera
* Ochrona przed atakami CSRF
* Kontrola wersji: Użycie systemu kontroli wersji (np. Git)
* Zarządzanie projektem: Możliwość użycia narzędzi takich jak Jira czy Trello do śledzenia postępów

# Podsumowanie
Aplikacja FuelBuddy ma na celu ułatwienie użytkownikom śledzenia i analizowania tankowań swoich pojazdów. Dzięki rozbudowanym funkcjom analitycznym i wizualizacjom danych, użytkownicy będą mogli efektywnie zarządzać kosztami eksploatacji oraz monitorować zużycie paliwa. Projekt zakłada zastosowanie nowoczesnych technologii i najlepszych praktyk programistycznych, co zapewni bezpieczeństwo, wydajność i skalowalność aplikacji.

# Pac-Man Java Code Reengineering  
*Tugas Akhir – Code Reengineering (SOAL A – Strict Copy Mode)*  

Proyek ini merupakan hasil **refaktorisasi terhadap kode PacMan.java** yang pada awalnya terdiri dari satu file besar berisi seluruh logic permainan. Proses refaktorisasi dilakukan untuk mengatasi berbagai **code smell**, meningkatkan modularitas, dan menerapkan prinsip desain yang benar **tanpa mengubah logika program** sesuai ketentuan soal.

Tujuan utama:  
- Menghapus code smells  
- Menerapkan teknik refactoring yang sesuai materi  
- Menjaga logika game 100% identik dengan versi asli  
- Menunjukkan penggunaan teknik reengineering seperti Extract Class, Extract Method, dan Introduce Constants

---

# 1. Soal yang Dikerjakan (SOAL A)

**Instruksi:**  
> Lakukan refactoring terhadap kode game Pac-Man berikut dengan menerapkan teknik code reengineering.  
> Logika program **tidak boleh diubah**, tetapi struktur, modularitas, keterbacaan, dan maintainability harus ditingkatkan.  
> Setiap perubahan harus memiliki dokumentasi:  
> - Jenis Code Smell  
> - Teknik Refactoring  
> - Alasan penggunaan teknik  
> - Dampak terhadap struktur kode  

Refactoring dilakukan dengan pendekatan **Strict Copy Mode**, yaitu:  
- **Tidak menambah fitur**  
- **Tidak mengubah logic**  
- **Tidak mengubah mekanisme permainan**  
- **Hanya memindahkan kode ke struktur yang lebih modular**  

---

# 2. Identifikasi Code Smell & Teknik yang Digunakan

### 2.1 God Class (Bloater)  
**Lokasi:** PacMan.java (sebelum refactor ± 700 baris)  
**Smell:**  
- Satu kelas berisi UI, game loop, entity logic, collision, input, map loader, rendering, state management  
- Melanggar Single Responsibility Principle  

**Teknik:**  
- *Extract Class*: GamePanel, Block, GameMap  
- *Extract Method*: loadImages(), move(), draw()  

**Hasil:**  
Struktur menjadi modular dan lebih mudah diuji, tanpa mengubah alur eksekusi maupun mekanik.

---

### 2.2 Long Method (Bloater)  
**Lokasi:**  
- loadMap()  
- move()  

**Smell:**  
- Method terlalu panjang, berisi banyak tahap proses (map parsing, entity creation, collision handling)  

**Teknik:**  
- *Extract Method*  
- *Extract Class (GameMap)*  

**Hasil:**  
Pengurangan kompleksitas method dan pemisahan tanggung jawab, tetap mempertahankan urutan eksekusi asli.

---

### 2.3 Magic Numbers (Primitive Obsession)  
**Lokasi:**  
- tileSize = 32  
- boardWidth = tileSize * columnCount  
- velocity Pac-Man = tileSize/4  
- Timer = 50ms  

**Teknik:**  
- *Introduce Constant → Constants.java*  

**Hasil:**  
Nilai konfigurasi terpusat, perubahan lebih aman, tidak mempengaruhi perilaku runtime.

---

### 2.4 Data Clump  
**Lokasi:** Map array + proses pemuatan map  
**Smell:**  
- Data map dan logic parsing selalu berpasangan  
- Kode sulit dipindah atau diuji  

**Teknik:**  
- *Extract Class → GameMap.java*  

**Hasil:**  
Struktur map lebih terorganisir, tetap mempertahankan isi map dan interpretasi karakter 100% sama.

---

### 2.5 Feature Envy  
**Lokasi:** updateDirection() mengakses walls yang berada di GamePanel  
**Teknik:**  
- *Move Method Parameter* (menambahkan walls sebagai argumen)  

**Hasil:**  
Mengurangi ketergantungan kelas tanpa memodifikasi logika original (move → undo → revert).

---

# 3. Struktur Folder Setelah Refactoring

PacmanRefactor/
│
├── App.java
├── GamePanel.java
├── GameMap.java
├── Block.java
├── Constants.java
│
├── wall.png
├── pacmanUp.png
├── pacmanDown.png
├── pacmanLeft.png
├── pacmanRight.png
├── blueGhost.png
├── orangeGhost.png
├── pinkGhost.png
├── redGhost.png

**Catatan:**  
Struktur dipertahankan sederhana **tanpa package**, sesuai instruksi dan kode asli.

---

# 4. Penjelasan File Hasil Refactoring

### **Block.java**  
- Representasi entitas asal (Pac-Man, ghost, wall, food)  
- Berisi updateDirection(), updateVelocity(), reset(), collision()  
- Logika identik dengan kode original (termasuk mekanisme "test movement then undo")

### **GameMap.java**  
- Memuat tileMap asli tanpa modifikasi  
- Memiliki fungsi loadMap() untuk menghasilkan walls, foods, ghosts, dan pacman  
- Menjaga mapping karakter → entity sama seperti kode awal

### **GamePanel.java**  
- Menggantikan peran kelas PacMan sebagai komponen permainan  
- Memegang rendering, loop, movement, collision, scoring  
- Semua logic sama seperti versi awal, hanya dipindahkan dengan lebih terstruktur

### **Constants.java**  
- Menyimpan tileSize, rowCount, columnCount, speed, timer delay  
- Menghapus magic numbers dari seluruh file lain

### **App.java**  
- Menangani bootstrap dan inisialisasi frame  
- Memisahkan tanggung jawab main method (kode asli mencampur UI dan game logic)

---

# 5. Cara Menjalankan Program

1. Letakkan semua file `.java` dan `.png` dalam folder yang sama.  
2. Compile semua class:
javac *.java

3. Jalankan:

Game akan berjalan **dengan perilaku identik** dengan versi PacMan.java awal.

---

# 6. Validasi: Perilaku Tidak Berubah

Semua mekanisme berikut telah diverifikasi tetap sama:

- Pergerakan Pac-Man (pixel per frame = tileSize/4)  
- updateDirection menggunakan model test-forward + undo collision  
- Ghost movement random + forced upward movement  
- Food 4×4 pixel + score 10  
- Lives system 3 → 0 → Game Over  
- Reset map & posisi entity saat food habis  
- Timer loop 50ms (20 FPS)  
- Rendering urutan sama  
- Semua collision identik  

---

# 7. Kesimpulan

Refactoring ini menunjukkan:

- Peningkatan signifikan dalam arsitektur kode  
- Penghilangan code smell tanpa mengganggu logika  
- Menerapkan berbagai teknik refactoring sesuai materi Code Reengineering  
- Mempertahankan kesepadanan perilaku (strict-copy mode)

Dengan struktur modular ini, game mudah dikembangkan lebih lanjut tanpa risiko merusak logika yang ada.

---

# 8. Lampiran  
Seluruh kode sumber dan dokumentasi teknik refactoring terdapat dalam masing-masing file `.java` melalui komentar:

```java
// SMELL: ...
// TECHNIQUE: ...
// REASON: ...
// RESULT: ...

---

Jika kamu mau, aku juga bisa buat:

✔ **PDF versi README**  
✔ **Laporan lengkap Bab 1–5** (Pendahuluan → Smell → Teknik → Pengujian → Kesimpulan)  
✔ **Diagram UML class**  

Tinggal bilang: **“Saya mau laporan lengkap”** atau **“Saya mau UML”**.


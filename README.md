# Pac-Man Java Code Reengineering

_Tugas Akhir – Code Reengineering (SOAL A – Strict Copy Mode)_

Proyek ini merupakan hasil **refaktorisasi terhadap kode PacMan.java** yang pada awalnya terdiri dari satu file besar berisi seluruh logic permainan. Proses refaktorisasi dilakukan untuk mengatasi berbagai **code smell**, meningkatkan modularitas, dan menerapkan prinsip desain yang benar **tanpa mengubah logika program** sesuai ketentuan soal.

Tujuan utama:

- Menghapus code smells
- Menerapkan teknik refactoring yang sesuai materi
- Menjaga logika game 100% identik dengan versi asli
- Menunjukkan penggunaan teknik reengineering seperti Extract Class, Extract Method, Introduce Constants, dan **Package Structuring**.

---

# 1. Soal yang Dikerjakan (SOAL A)

**Instruksi:**

> Lakukan refactoring terhadap kode game Pac-Man berikut dengan menerapkan teknik code reengineering.  
> Logika program **tidak boleh diubah**, tetapi struktur, modularitas, keterbacaan, dan maintainability harus ditingkatkan.  
> Setiap perubahan harus memiliki dokumentasi:
>
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

- _Extract Class_: GamePanel, Block, GameMap
- _Extract Method_: loadImages(), move(), draw()

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

- _Extract Method_
- _Extract Class (GameMap)_

**Hasil:**  
Pengurangan kompleksitas method dan pemisahan tanggung jawab, tetap mempertahankan urutan eksekusi asli.

---

### 2.3 Magic Numbers (Primitive Obsession)

**Lokasi:**

- tileSize = 32
- boardWidth = tileSize \* columnCount
- velocity Pac-Man = tileSize/4
- Timer = 50ms

**Teknik:**

- _Introduce Constant → Constants.java_

**Hasil:**  
Nilai konfigurasi terpusat, perubahan lebih aman, tidak mempengaruhi perilaku runtime.

---

### 2.4 Data Clump

**Lokasi:** Map array + proses pemuatan map  
**Smell:**

- Data map dan logic parsing selalu berpasangan
- Kode sulit dipindah atau diuji

**Teknik:**

- _Extract Class → GameMap.java_

**Hasil:**  
Struktur map lebih terorganisir, tetap mempertahankan isi map dan interpretasi karakter 100% sama.

---

### 2.5 Feature Envy

**Lokasi:** updateDirection() mengakses walls yang berada di GamePanel  
**Teknik:**

- _Move Method Parameter_ (menambahkan walls sebagai argumen)

**Hasil:**  
Mengurangi ketergantungan kelas tanpa memodifikasi logika original (move → undo → revert).

---

### 2.6 Poor Package Structure (Organizational Smell)

**Lokasi:** Flat file structure (semua file di root)
**Smell:**

- Class-class tidak dikelompokkan berdasarkan fungsinya.
- Sulit untuk membedakan antara Model, View, dan Utility.

**Teknik:**

- _Move Class_ / _Package Structuring_

**Hasil:**

- Kode diorganisir ke dalam package `com.pacman.model`, `com.pacman.view`, `com.pacman.constant`.
- Mengikuti Java Best Practice.

---

# 3. Struktur Folder Setelah Refactoring

Struktur proyek kini mengikuti standar _best practice_ Java dengan penggunaan package:

```
com/
└── pacman/
    ├── App.java                  // Entry Point
    ├── constant/                 // Constants & Enums
    │   ├── Constants.java
    │   └── Direction.java
    ├── model/                    // Game Logic & Data
    │   ├── Block.java
    │   ├── GameMap.java
    │   └── GameModel.java
    └── view/                     // UI & Visualization
        └── GamePanel.java
```

File resource (gambar) tetap berada di root directory atau sesuai konfigurasi resources:

- `wall.png`
- `pacmanUp.png`, dll.

---

# 4. Penjelasan File Hasil Refactoring

### **com.pacman.model.Block**

- Representasi entitas asal (Pac-Man, ghost, wall, food)
- Berisi updateDirection(), updateVelocity(), reset(), collision()
- Logika identik dengan kode original

### **com.pacman.model.GameMap**

- Memuat tileMap asli tanpa modifikasi
- Memiliki fungsi loadMap() untuk menghasilkan walls, foods, ghosts, dan pacman

### **com.pacman.model.GameModel**

- Mengelola state permainan (score, lives, game over logic)
- Memisahkan logika permainan dari tampilan (View)

### **com.pacman.view.GamePanel**

- Menangani rendering, input listener (Key Adapter)
- Mengambil data dari GameModel untuk ditampilkan

### **com.pacman.constant.Constants & Direction**

- Menyimpan konfigurasi global dan enum arah pergerakan

### **com.pacman.App**

- Menangani bootstrap dan inisialisasi JFrame

---

# 5. Cara Menjalankan Program

1. Pastikan berada di root directory project (folder di mana folder `com` berada dan file gambar `.png` berada).

2. **Compile** project:

   ```bash
   javac com/pacman/App.java
   ```

   _(Atau compile semua file jika perlu: `javac com/pacman/\*\*/_.java`)\*

3. **Jalankan** program:
   ```bash
   java com.pacman.App
   ```

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
- **Penerapan struktur package yang rapi (Model-View Separation)**
- Mempertahankan kesepadanan perilaku (strict-copy mode)

Dengan struktur modular ini, game mudah dikembangkan lebih lanjut tanpa risiko merusak logika yang ada.

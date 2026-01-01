# ==========================================
# Dragon Book Study Artifact Generator (Dir Version)
# ==========================================

SRC = hello.c
NAME = hello
INFO_FILE = $(OUT_DIR)/00_build_env.txt

# システム情報の取得
# UNAME_S: OS名 (例: Darwin, Linux)
# UNAME_M: アーキテクチャ (例: arm64, x86_64)
UNAME_S := $(shell uname -s)
UNAME_M := $(shell uname -m)

# 出力先ディレクトリ名
# 例: Darwin-arm64-artifacts
OUT_DIR = $(UNAME_S)-$(UNAME_M)

# デフォルトターゲット
all: prepare info step0 step1 step2 step3 step4 step_macos

# ------------------------------------------
# Preparation
# 出力用ディレクトリを作成
# ------------------------------------------
prepare:
	@echo "Creating directory: $(OUT_DIR)"
	@mkdir -p $(OUT_DIR)

# ------------------------------------------
# Phase Info: Environment Dump
# ビルド環境の情報を記録
# ------------------------------------------
info:
	@echo "Generating Build Info..."
	@echo "=== 1. OS Info (uname -a) ===" > $(INFO_FILE)
	@uname -a >> $(INFO_FILE) 2>&1
	
	@echo "" >> $(INFO_FILE)
	@echo "=== 2. Compiler Version (gcc --version) ===" >> $(INFO_FILE)
	@gcc --version >> $(INFO_FILE) 2>&1
	
	@echo "" >> $(INFO_FILE)
	@echo "=== 3. Compiler Path (which gcc) ===" >> $(INFO_FILE)
	@which gcc >> $(INFO_FILE) 2>&1
	
	@echo "" >> $(INFO_FILE)
	@echo "=== 4. Linker Version (ld -v) ===" >> $(INFO_FILE)
	@ld -v >> $(INFO_FILE) 2>&1

	@echo "" >> $(INFO_FILE)
	@echo "=== 5. CPU Info ===" >> $(INFO_FILE)
ifeq ($(UNAME_S),Darwin)
	@sysctl -n machdep.cpu.brand_string >> $(INFO_FILE) 2>&1 || echo "sysctl failed" >> $(INFO_FILE)
else
	@lscpu >> $(INFO_FILE) 2>&1 || echo "lscpu failed" >> $(INFO_FILE)
endif

	@echo "" >> $(INFO_FILE)
	@echo "=== 6. Predefined Macros (Default #defines) ===" >> $(INFO_FILE)
	@echo "To see differences in #ifdef behaviors" >> $(INFO_FILE)
	@gcc -dM -E - < /dev/null >> $(INFO_FILE) 2>&1

# ------------------------------------------
# Phase 0: Source Snapshot
# ------------------------------------------
step0:
	@echo "Phase 0: Snapshotting Source..."
	cp $(SRC) $(OUT_DIR)/00_$(NAME).c

# ------------------------------------------
# Phase 1: Preprocessing
# ------------------------------------------
step1:
	@echo "Phase 1: Preprocessing..."
	gcc -E $(SRC) > $(OUT_DIR)/01_$(NAME).i

# ------------------------------------------
# Phase 2: Compilation (to Assembly)
# ------------------------------------------
step2:
	@echo "Phase 2: Compiling to Assembly..."
	gcc -S $(SRC) -o $(OUT_DIR)/02_$(NAME).s

# ------------------------------------------
# Phase 3: Assembly (to Object)
# ------------------------------------------
step3:
	@echo "Phase 3: Assembling to Object..."
	gcc -c $(SRC) -o $(OUT_DIR)/03_$(NAME).o
	# 解析ログ
	objdump -d $(OUT_DIR)/03_$(NAME).o > $(OUT_DIR)/03_$(NAME).o.disasm.txt
	objdump -r $(OUT_DIR)/03_$(NAME).o > $(OUT_DIR)/03_$(NAME).o.reloc.txt
	nm -n $(OUT_DIR)/03_$(NAME).o > $(OUT_DIR)/03_$(NAME).o.symbols.txt

# ------------------------------------------
# Phase 4: Linking (to Executable)
# ------------------------------------------
step4:
	@echo "Phase 4: Linking..."
	# マップファイルの出力パスもディレクトリ内に指定
	gcc $(OUT_DIR)/03_$(NAME).o -o $(OUT_DIR)/04_$(NAME).bin -Wl,-map,$(OUT_DIR)/04_$(NAME).bin.map.txt
	# 解析ログ
	objdump -d $(OUT_DIR)/04_$(NAME).bin > $(OUT_DIR)/04_$(NAME).bin.disasm.txt
	nm -n $(OUT_DIR)/04_$(NAME).bin > $(OUT_DIR)/04_$(NAME).bin.symbols.txt

# ------------------------------------------
# OS Specific: macOS Only
# ------------------------------------------
step_macos:
ifeq ($(UNAME_S),Darwin)
	@echo "macOS detected: Generating otool logs..."
	otool -L $(OUT_DIR)/04_$(NAME).bin > $(OUT_DIR)/04_$(NAME).bin.deps.txt
	otool -l $(OUT_DIR)/04_$(NAME).bin > $(OUT_DIR)/04_$(NAME).bin.loadcmds.txt
else
	@echo "Not macOS: Skipping otool steps."
endif

# ------------------------------------------
# Clean
# ------------------------------------------
clean:
	@echo "Cleaning up $(OUT_DIR)..."
	rm -rf $(OUT_DIR)
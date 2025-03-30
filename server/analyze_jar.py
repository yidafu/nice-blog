import os
import zipfile
from collections import defaultdict


def convert_size(size_bytes):
    """
    将字节大小转换为更友好的单位（KB、MB、GB）。
    """
    if size_bytes < 1024:
        return f"{size_bytes} bytes"
    elif size_bytes < 1024 * 1024:
        return f"{size_bytes / 1024:.2f} KB"
    elif size_bytes < 1024 * 1024 * 1024:
        return f"{size_bytes / (1024 * 1024):.2f} MB"
    else:
        return f"{size_bytes / (1024 * 1024 * 1024):.2f} GB"

def analyze_jar(jar_path):
    """
    分析 JAR 文件中每个包的大小。
    """
    sizes = defaultdict(int)
    with zipfile.ZipFile(jar_path, 'r') as jar:
        for file in jar.infolist():
            # 按包（目录）分组
            package = os.path.dirname(file.filename)
            sizes[package] += file.file_size
    return sizes

def print_sorted_sizes(sizes):
    """
    按大小排序并打印结果。
    """
    for package, size in sorted(sizes.items(), key=lambda x: x[1], reverse=True):
      if (size > 1024 * 100):
        print(f"{convert_size(size):>10}  {package}")
if __name__ == "__main__":
    jar_path = "build/libs/server-1.0.0-SNAPSHOT-fat.jar"  # 替换为你的 JAR 文件路径
    sizes = analyze_jar(jar_path)
    print_sorted_sizes(sizes)


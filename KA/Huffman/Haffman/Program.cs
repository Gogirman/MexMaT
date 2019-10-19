﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using HuffmanFileWork;

namespace Huffman
{
    class HuffmanTree
    {
        public char ch { get; private set; }
        public double freq { get; private set; }
        public bool isTerminal { get; private set; }
        public HuffmanTree left { get; private set; }
        public HuffmanTree rigth { get; private set; }
        public HuffmanTree(char c, double frequency)
        {
            ch = c;
            freq = frequency;
            isTerminal = true;
            left = rigth = null;
        }
        public HuffmanTree(HuffmanTree l, HuffmanTree r)
        {
            freq = l.freq + r.freq;
            isTerminal = false;
            left = l;
            rigth = r;
        }
    }

    class HuffmanInfo
    {
        HuffmanTree Tree; // дерево кода Хаффмана, потребуется для распаковки
        Dictionary<char, string> Table; // словарь, хранящий коды всех символов, будет удобен для сжатия

        private void filling_table(HuffmanTree current_node, string code)
        {
            if (!current_node.isTerminal)
            {
                filling_table(current_node.left, code + "0");
                filling_table(current_node.rigth, code + "1");
            }
            else
            {
                Table[current_node.ch] = code;
            }
        }
        public HuffmanInfo(string fileName)
        {
            // Храним все считанные частоты
            List<HuffmanTree> huffman_node = new List<HuffmanTree>();
            string line;
            StreamReader sr = new StreamReader(fileName, Encoding.Unicode);
            // считать информацию о частотах символов
            while ((line = sr.ReadLine()) != null)
            {
                if (line.Length == 0)
                {
                    //TODO: отдельная обработка строки, которой соответствует частота символа "конец строки" 
                    line = sr.ReadLine().Trim();
                    double freq = Convert.ToDouble(line);
                    huffman_node.Add(new HuffmanTree('\n', freq));
                }
                else
                {
                    //TODO: создаем вершину (лист) дерева с частотой очередного символа
                    char ch = line[0];
                    double freq = Convert.ToDouble(line.Substring(2));
                    huffman_node.Add(new HuffmanTree(ch, freq));
                }
            }
            sr.Close();
            // TODO: добавить еще одну вершину-лист, соответствующую символу с кодом 0 ('\0'), который будет означать конец файла. 
            // Частота такого символа, очевидно, должна быть очень маленькой, т.к. такой символ встречается только 1 раз во всем файле (можно даже сделать частоту = 0)
            huffman_node.Add(new HuffmanTree('\0', 0));
            // TODO: построить дерево кода Хаффмана путем последовательного объединения листьев
            while (huffman_node.Count > 1)
            {
                huffman_node = (from node in huffman_node
                                orderby node.freq
                                select node).ToList();
                var new_node = new HuffmanTree(huffman_node[0], huffman_node[1]);
                huffman_node.RemoveAt(1);
                huffman_node.RemoveAt(0);
                huffman_node.Add(new_node);
            }
            Tree = huffman_node[0];
            Table = new Dictionary<char, string>();
            // TODO: заполнить таблицу кодирования Table на основе обхода построенного дерева
            filling_table(Tree, "");
            //Table.Add('\0', "0"); // это временная заглушка!!! Эту строчку нужно будет потом убрать, т.к. признак конца файла должен быть уже добавлен в таблицу, как и все остальные символы
        }
        public void Compress(string inpFile, string outFile)
        {
            var sr = new StreamReader(inpFile, Encoding.Unicode);
            var sw = new ArchWriter(outFile); //нужна побитовая запись, поэтому использовать StreamWriter напрямую нельзя
            string line;
            while ((line = sr.ReadLine()) != null)
            {
                // TODO: посимвольно обрабатываем строку, кодируем, пишем в sw
                string code = "";
                foreach (char symbol in line)
                {
                    code += Table[symbol];
                }
                code += Table['\n'];
                sw.WriteWord(code);
            }
            sr.Close();
            sw.WriteWord(Table['\0']); // записываем признак конца файла
            sw.Finish();
        }
        public void Decompress(string archFile, string txtFile)
        {
            var sr = new ArchReader(archFile); // нужно побитовое чтение
            var sw = new StreamWriter(txtFile, false, Encoding.Unicode);
            byte curBit; string code = "";
            while (sr.ReadBit(out curBit))
            {
                // TODO: побитово (!) разбираем архив
                if (curBit == 0)
                {
                    code += "0";
                }
                else if (curBit == 1)
                {
                    code += "1";
                }

                if (Table.ContainsValue(code))
                {
                    char symbol = Table.Where(x => x.Value == code).FirstOrDefault().Key;
                    sw.Write(symbol);
                    code = "";
                }
            }
            sr.Finish();
            sw.Close();
        }
    }

    class Huffman
    {
        static void Main(string[] args)
        {
            if (!File.Exists("freq.txt"))
            {
                Console.WriteLine("Не найден файл с частотами символов!");
                return;
            }
            if (args.Length == 0)
            {
                var hi = new HuffmanInfo("freq.txt");
                hi.Compress("etalon.txt", "etalon.arc");
                hi.Decompress("etalon.arc", "etalon_dec.txt");
                return;
            }
            if (args.Length != 3 || args[0] != "zip" && args[0] != "unzip")
            {
                Console.WriteLine("Синтаксис:");
                Console.WriteLine("Huffman.exe zip <имя исходного файла> <имя файла для архива>");
                Console.WriteLine("либо");
                Console.WriteLine("Huffman.exe unzip <имя файла с архивом> <имя файла для текста>");
                Console.WriteLine("Пример:");
                Console.WriteLine("Huffman.exe zip text.txt text.arc");
                return;
            }
            var HI = new HuffmanInfo("freq.txt");
            if (args[0] == "zip")
            {
                if (!File.Exists(args[1]))
                {
                    Console.WriteLine("Не найден файл с исходным текстом!");
                    return;
                }
                HI.Compress(args[1], args[2]);
            }
            else
            {
                if (!File.Exists(args[1]))
                {
                    Console.WriteLine("Не найден файл с архивом!");
                    return;
                }
                HI.Decompress(args[1], args[2]);
            }
            Console.WriteLine("Операция успешно завершена!");
        }
    }
}
final BASE_FOLDER = /C:\Users\trnka\Documents\Survey\Articles/

File baseDirectory = new File(BASE_FOLDER)

//iterates through folders in base directory and process each file in them
baseDirectory.eachDir { dir -> dir.eachFile { file -> convertPdfToTxt(file) } }

//convertPdfToTxt(new File(/C:\Users\trnka\Documents\Survey\Articles\ACM\A Flexible Authorization Architecture for Systems of Interoperable Medical Devices.pdf/))

void convertPdfToTxt(File file) {
    if (file.getName().endsWith(".txt")) {
        return
    }
    //create same file name, just with .txt extension
    def newName = file.path.take(file.path.lastIndexOf(".")) + ".txt"

    //new File(newName).delete()

    // magic with $/ escaped string /$
    println($/pdftotext -clip "$file.path" "${newName}/$.execute().text)

    def createdFile = new File(newName)
    createdFile.write(processText(createdFile.text))
}

//strip lines that are shorter than 25 chars
String processText(String text) {
    text = removeCitationsFromTheEnd(text)
    def sb = '' << '' //concat empty string to empty string to create string buffer
    text.eachLine { line ->
        if (line.length() > 25) {
            sb <<= line.trim()
            sb <<= System.lineSeparator()//" " //so the word from different lines are not concated
        }
    }
    return sb
}

//reverse line order and then remove lines starting with [ until normal text appears
String removeCitationsFromTheEnd(String text) {
    def reversed = reverseLines(text)
    def lines = reversed.split("\n")

    def citations = true
    def buffer = '' << ''
    for (def i = 0; i < lines.length; i++) {
        def line = lines[i].trim()
        if (line.empty == true) {
            continue
        }
        if (citations == false) {
            buffer <<= line
            buffer <<= System.lineSeparator()
            continue
        }
        if (line.startsWith("[")) {
            continue
        }
        //some citations are up to 3 lines long
        if (checkLines(lines, i, 5)) {
            continue
        }
        buffer <<= line
        buffer <<= System.lineSeparator()
        citations = false
    }
    return reverseLines(buffer.toString())
}

//reverse lines order
String reverseLines(String text) {
    def sb = '' << ''
    text.eachLine { line ->
        if (line.empty == true) {
            return
        }
        sb <<= line.reverse()
        sb <<= "\n"
    }
    return sb.reverse()
}

boolean checkLines(String[] lines, int start, int amount) {
    def end = start + amount
    for (def i = start; i <= end; i++) {
        if (lines[i].trim().startsWith("[")) {
            return true
        }
    }
    return false
}



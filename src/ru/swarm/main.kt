package ru.swarm

import java.awt.Desktop
import java.awt.Dimension
import java.awt.MouseInfo
import java.awt.Toolkit
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.io.File
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextField
import javax.swing.ScrollPaneConstants

var files = JList<String>()
var filesContent = ArrayList<String>()
var go = JButton("go")
var terminal = JButton("terminal")
var back = JButton("back")
var next = JButton("next")
var delete = JButton("delete")
var create = JButton("create")

var buffer = ArrayList<String>()
var bufferActive = 0;
var currentDirectory = "/home/"+System.getProperty("user.name")
var directoryField = JTextField(40)
var array =  filesContent.toTypedArray()
var toolkit = Toolkit.getDefaultToolkit()
var defaultWidth = 800
var defaultHeight = 600



@JvmName("getWindow1")
fun getWindow() : JFrame {
    var window = JFrame()
    window.setLocation(MouseInfo.getPointerInfo().location.x-defaultWidth/2, MouseInfo.getPointerInfo().location.y-defaultHeight/2)
    window.setSize(defaultWidth, defaultHeight);
    window.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    return window
}

var window = getWindow()

class backAct : ActionListener {
    override fun actionPerformed(e: ActionEvent?) {
        if (!buffer.isEmpty() && bufferActive!=0) {
            if(bufferActive==buffer.size) buffer.add(currentDirectory)
            bufferActive--;
            currentDirectory= buffer[bufferActive]
            directoryField.text= currentDirectory
            window.title = "$currentDirectory YileExlorer"
            files.setListData(File(currentDirectory).list())
        }
    }

}
class nextAct : ActionListener {
    override fun actionPerformed(e: ActionEvent?) {
        if (!buffer.isEmpty() && bufferActive!=buffer.size) {
            bufferActive++;
            if (bufferActive== buffer.size) {
                //bufferActive-=1
                //buffer.add(currentDirectory)
            } else {
                currentDirectory = buffer[bufferActive]
                directoryField.text = currentDirectory
                window.title = "$currentDirectory YileExlorer"
                files.setListData(File(currentDirectory).list())
            }
        }
    }

}
class deleteAct : ActionListener {
    override fun actionPerformed(e: ActionEvent?) {

        Runtime.getRuntime().exec(("rm -r'"+directoryField.text+"/"+files.selectedValue+"'"))
    }

}
class terminalAct : ActionListener {
    override fun actionPerformed(e: ActionEvent?) {
        Runtime.getRuntime().exec(("konsole '"+directoryField.text+"/"+files.selectedValue+"'"))

    }

}
class createAct : ActionListener {
    override fun actionPerformed(e: ActionEvent?) {
    }

}

class goAct : ActionListener {
    override fun actionPerformed(e: ActionEvent?) {
        if (currentDirectory.equals(directoryField.text)) {
            if (File(directoryField.text+"/"+files.selectedValue).isDirectory) {
                buffer.add(currentDirectory)
                directoryField.text += "/" + files.selectedValue
                currentDirectory = directoryField.text
                if (currentDirectory.isEmpty()) currentDirectory = "/"
                window.title = "$currentDirectory YileExlorer"
                files.setListData(File(currentDirectory).list())

            } else {
                Desktop.getDesktop().open(File(directoryField.text+"/"+files.selectedValue))
                //Runtime.getRuntime().exec(directoryField.text+"/"+files.selectedValue);
            }
        } else {
            buffer.add(currentDirectory)
            currentDirectory = directoryField.text
            if (currentDirectory.isEmpty()) currentDirectory = "/"
            window.title = "$currentDirectory YileExlorer"
            files.setListData(File(currentDirectory).list())

        }

        bufferActive = buffer.size-1
        if (bufferActive<1) {
            bufferActive=1
        }
    }
}

fun getRootPanel() : JPanel {
    var panel = JPanel()
    var fileArea = JPanel()
    var drives = JPanel()

    panel.add(drives)
    var fileScroll = JScrollPane(fileArea)
    var toolkit = Toolkit.getDefaultToolkit()
    fileScroll.verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS
    fileScroll.horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS

    fileScroll.size = Dimension(200, 300)
    fileScroll.maximumSize = Dimension(200, 300)
    fileScroll.minimumSize = Dimension(200, 300)
    fileScroll.preferredSize = Dimension(200, 300)
    panel.add(directoryField)
    panel.add(go)
    panel.add(terminal)
    panel.add(delete)
    panel.add(create)
    panel.add(back)
    panel.add(next)
    directoryField.text = currentDirectory
    panel.add(fileScroll)
    //panel.add(fileArea)
    fileArea.add(files)

    return panel
}

fun main(args: Array<String>) {


    files.setListData(File(currentDirectory).list())

    window.title = "$currentDirectory YileExlorer"
    go.addActionListener(goAct())
    terminal.addActionListener(terminalAct())
    delete.addActionListener(deleteAct())
    create.addActionListener(createAct())
    back.addActionListener(backAct())
    next.addActionListener(nextAct())
    window.add(getRootPanel())
    window.isResizable = false
    window.repaint()
    window.show()
}
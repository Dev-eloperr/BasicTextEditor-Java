package main;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.Scanner;
import javax.swing.JMenu;
import javax.swing.text.*;
//Text Editor

public class Main {
    public static String Shape = new String();          //To select shape for insertt function
    public static JTextPane textArea = new JTextPane();    //Setting textpane as default editing area to implement attributes late on
    public static int index =0;                          //Default index to start find function
    public static String fileName="";                    //Initial file name...could be dited
    public static DisplayShapes displayShapes = new DisplayShapes();


    public static void main(String[] args) {
        Editor x_temp = new Editor();         //editor object
        JFrame editor = new JFrame();         //main frame
        editor.add(x_temp);                   //adding edtor window
        //editor.setBackground(Color.white);
        editor.setTitle("Text Editor");
        editor.setSize(1500,900);
        editor.setLocationRelativeTo(null);
        editor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        editor.setVisible(true);
    }

}

class WordCount extends JPanel{
    static  JLabel Count = new JLabel(Integer.toString(0));              //Count to store number of words
    static Font font = new Font("Microsoft Sans Serif", Font.PLAIN, 10);  //font for jcomponent
    WordCount() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel wordsString = new JLabel(" Words: ");

        wordsString.setFont(font);
        setBorder(BorderFactory.createLoweredBevelBorder());
        add(wordsString);

        String copyTextArea = new String();
        copyTextArea = Main.textArea.getText();

        Count.setFont(font);
        add(Count);  //implementation in main editor class
    }

}

class Editor extends JPanel {
    Editor(){

        setLayout(new BorderLayout(10, 0));

        WordCount wordCount = new WordCount();

        ActionListener action = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Main.textArea.getText()==""){          //if null
                    WordCount.Count.setText(Integer.toString(0));
                }
                else if(Main.textArea.getSelectionEnd()!=Main.textArea.getSelectionStart()){   //Displays total word count or the selected word count
                    String[] split = Main.textArea.getSelectedText().trim().split("\\p{javaSpaceChar}{1,}"); //counting words on the basis of split of one spacebar
                    //System.out.println(split.length);
                    WordCount.Count.setText(Integer.toString(split.length));
                }
                else{
                    String[] split = Main.textArea.getText().trim().split("\\p{javaSpaceChar}{1,}");
                    //System.out.println(split.length);
                    WordCount.Count.setText(Integer.toString(split.length));
                }
            }
        };
        Timer t = new Timer(100, action);   //refreshes text field every time to keep it updated
        t.start();
        MenuBar menuBar = new MenuBar();          //setting locations of various editor components
        add(menuBar,BorderLayout.NORTH);
        DisplayField displayField = new DisplayField();
        add(displayField,BorderLayout.WEST);
        add(Main.displayShapes,BorderLayout.CENTER);
        add(wordCount,BorderLayout.SOUTH);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.WHITE);              //editor color as white
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 10, Color.white)); //right border as white of width 10 units
    }
}

class FormatBar extends JPanel{
    static String []sizes = {"5","5.5","6.5","7.5","8","9","10","11","12","14","16","18","20","22","24","26","28","36","48","72"};
    static JComboBox Size = new JComboBox(sizes);   //adding array of text sizes in a jcombo box
    static JComboBox Fonts = new JComboBox(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
    //getting the list of default/ added fonts on a particular system
    FormatBar(){

        setLayout(new FlowLayout(FlowLayout.LEFT));
        AttributeSet attributeSet = Main.textArea.getCharacterAttributes();  //assigning the attributes available to textarea in a attribute variable
        JMenuBar formatbar = new JMenuBar();
        JLabel F = new JLabel("Font");        //adding font combobox in formatbar
        add(F);
        Fonts.setSelectedItem("Arial");      //default font
        Size.setSelectedItem("12");          //defaullt size

        Fonts.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = Fonts.getSelectedItem().toString();   //getting name of selected font
                new StyledEditorKit.FontFamilyAction("font-family-" + name, name).actionPerformed(e); //setting font  using styled editor kit
                Main.textArea.requestFocus();  //requesting focus in editor window
            }
        });
        JLabel T = new JLabel("Size");   //size
        add(Fonts);
        add(T);  //adding size label
        Size.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = Size.getSelectedItem().toString();   //getting name of selected size
                int size = Integer.parseInt(s);    //converting string to integer
                Action fontAction = new StyledEditorKit.FontSizeAction(String.valueOf(size), size);//setting size  using styled editor kit
                fontAction.actionPerformed(e);
            }
        });
        add(Size);
        JButton Underline=new JButton(" U  ");
        Underline.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StyledDocument doc = (StyledDocument) Main.textArea.getDocument();  //using styled document to preserve attributes
                int selectionEnd = Main.textArea.getSelectionEnd();    //selection end
                int selectionStart = Main.textArea.getSelectionStart();  //selection start
                if (selectionStart == selectionEnd) {            //if nothing is selected
                    return;
                }
                Element element = doc.getCharacterElement(selectionStart); //Gets the element that represents the character that is at the given offset within the document.
                AttributeSet as = element.getAttributes();   //copying attributes of the selected text

                MutableAttributeSet asNew = new SimpleAttributeSet(as.copyAttributes());  //interface for a mutable collection of unique attributes.
                StyleConstants.setUnderline(asNew, !StyleConstants.isUnderline(as));  //applying underline attribute to attribute set as
                doc.setCharacterAttributes(selectionStart, Main.textArea.getSelectedText().length(), asNew, true);  //using mutable attributes replacing text in textarea format
            }
        });
        JButton Bold = new JButton(" B  ");
        Bold.setPreferredSize(new Dimension(30,30));

        Bold.addActionListener(new ActionListener() {      //functionality remain same as underline function refer line 133
            @Override
            public void actionPerformed(ActionEvent e) {


                StyledDocument doc = (StyledDocument) Main.textArea.getDocument();
                int selectionEnd = Main.textArea.getSelectionEnd();
                int selectionStart = Main.textArea.getSelectionStart();
                if (selectionStart == selectionEnd) {
                    return;
                }
                Element element = doc.getCharacterElement(selectionStart);
                AttributeSet as = element.getAttributes();

                MutableAttributeSet asNew = new SimpleAttributeSet(as.copyAttributes());
                StyleConstants.setBold(asNew, !StyleConstants.isBold(as));
                doc.setCharacterAttributes(selectionStart, Main.textArea.getSelectedText().length(), asNew, true);
            }
        });
        JButton Italics = new JButton("  I  ");        //functionality remain same as underline function refer line 133
        Italics.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                StyledDocument doc = (StyledDocument) Main.textArea.getDocument();
                int selectionEnd = Main.textArea.getSelectionEnd();
                int selectionStart = Main.textArea.getSelectionStart();
                if (selectionStart == selectionEnd) {
                    return;
                }
                Element element = doc.getCharacterElement(selectionStart);
                AttributeSet as = element.getAttributes();

                MutableAttributeSet asNew = new SimpleAttributeSet(as.copyAttributes());
                StyleConstants.setItalic(asNew, !StyleConstants.isItalic(as));
                doc.setCharacterAttributes(selectionStart, Main.textArea.getSelectedText().length(), asNew, true);
            }
        });
        Bold.setBackground(Color.white);                                                   //setting aesthetics of b/i/u buttons
        Bold.setBorder(BorderFactory.createBevelBorder(1, Color.lightGray, Color.white));
        Italics.setBackground(Color.white);
        Italics.setBorder(BorderFactory.createBevelBorder(1, Color.lightGray, Color.white));
        Underline.setBackground(Color.white);
        Underline.setBorder(BorderFactory.createBevelBorder(1, Color.lightGray, Color.white));
        add(Bold);
        add(Italics);
        add(Underline);
        Italics.setPreferredSize(new Dimension(30,30));
        Underline.setPreferredSize(new Dimension(30,30));
    }
    @Override
    protected void paintComponent(Graphics g){    //background color as white
        super.paintComponent(g);
        setBackground(Color.white);
    }
}

class MenuBar extends JPanel implements ActionListener{
    MenuBar() {

        setLayout(new GridLayout(2,1,0,0)); //for menubar and foormatbar
        JMenuBar menuBar = new JMenuBar();
        FormatBar formatBar = new FormatBar();
        menuBar.setBackground(Color.white);       //change default path for icon
        ImageIcon find = new ImageIcon("D:\\Windows\\Documents\\NetBeansProjects\\Main\\dist\\find.png");  //setting icons
        ImageIcon findall = new ImageIcon("D:\\Windows\\Documents\\NetBeansProjects\\Main\\dist\\findall.png");
        ImageIcon replaceall = new ImageIcon("D:\\Windows\\Documents\\NetBeansProjects\\Main\\dist\\replaceall.png");

        ImageIcon iconNew = new ImageIcon("D:\\Windows\\Documents\\NetBeansProjects\\Main\\dist\\New.png");
        ImageIcon iconOpen = new ImageIcon("D:\\Windows\\Documents\\NetBeansProjects\\Main\\dist\\Open.png");
        ImageIcon iconSave = new ImageIcon("D:\\Windows\\Documents\\NetBeansProjects\\Main\\dist\\Save.png");
        ImageIcon iconCut = new ImageIcon("D:\\Windows\\Documents\\NetBeansProjects\\Main\\dist\\cut.png");
        ImageIcon iconCopy = new ImageIcon("D:\\Windows\\Documents\\NetBeansProjects\\Main\\dist\\copy.png");
        ImageIcon iconPaste = new ImageIcon("D:\\Windows\\Documents\\NetBeansProjects\\Main\\dist\\paste.png");
        ImageIcon iconSelect = new ImageIcon("D:\\Windows\\Documents\\NetBeansProjects\\Main\\dist\\select.png");

        Font font = new Font(TOOL_TIP_TEXT_KEY, Font.PLAIN, 15);

        JMenu File = new JMenu("File");   //file button
        JMenu Edit = new JMenu("Edit");   //edit list

        JMenu Insert = new JMenu("Insert");
        JMenu Format = new JMenu("Format");

        JMenu Help = new JMenu("Help");

        File.setFont(font);          //setting a common font as described above for better looks
        Edit.setFont(font);
        Insert.setFont(font);
        Format.setFont(font);
        Help.setFont(font);

        JMenuItem oval = new JMenuItem("Oval");         //adding basic shapes
        JMenuItem quadrilateral = new JMenuItem("quadrilateral");
        JMenuItem triangle = new JMenuItem("Triangle");
        JMenuItem Pentagon = new JMenuItem("Pentagon");
        JMenuItem Hexagon = new JMenuItem("Hexagon");
        JMenuItem Clean = new JMenuItem("Clean");

        JMenuItem Uppercase = new JMenuItem("UpperCase");   //functionality 1&2
        JMenuItem Lowercase = new JMenuItem("LowerCase");

        /**
         * Uppercase and lowercase implementation.
         * to retain the attributes first copy the attributes of individual chars
         * in a string. Then store them in an array of attributes.
         * then replace the part of string with either lowercase or uppercase
         * and then set the attributes as it is lost when the string is replaced.
         */
        Uppercase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int iStart = Main.textArea.getSelectionStart();     //getting selection start and end
                int iEnd = Main.textArea.getSelectionEnd();

                AttributeSet set[]= new AttributeSet[iEnd-iStart];
                for(int i = 0; i < iEnd-iStart; i++) {
                    set[i] = Main.textArea.getStyledDocument().getCharacterElement(i+iStart).getAttributes();
                }
                Main.textArea.replaceSelection(Main.textArea.getSelectedText().toUpperCase());
                for(int i = 0; i < set.length; i++) {
                    Main.textArea.getStyledDocument().setCharacterAttributes(iStart,set.length,set[i],true);
                }

            }});
        Lowercase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int iStart = Main.textArea.getSelectionStart();
                int iEnd = Main.textArea.getSelectionEnd();

                AttributeSet set[]= new AttributeSet[iEnd-iStart];
                for(int i = 0; i < iEnd-iStart; i++) {
                    set[i] = Main.textArea.getStyledDocument().getCharacterElement(i+iStart).getAttributes();
                }
                Main.textArea.replaceSelection(Main.textArea.getSelectedText().toLowerCase());
                for(int i = 0; i < set.length; i++) {
                    Main.textArea.getStyledDocument().setCharacterAttributes(iStart,set.length,set[i],true);
                }            }
        });

        Insert.add(oval);
        Insert.add(triangle);
        Insert.add(quadrilateral);
        Insert.add(Hexagon);
        Insert.add(Pentagon);
        Insert.add(Clean);

        oval.addActionListener(this);
        triangle.addActionListener(this);
        quadrilateral.addActionListener(this);
        Pentagon.addActionListener(this);
        Hexagon.addActionListener(this);
        Clean.addActionListener(this);

        JMenuItem Find = new JMenuItem(" Find");
        JMenuItem FindReplace = new JMenuItem(" Find & Replace");
        JMenuItem ReplaceAll = new JMenuItem(" Replace All");
        /**
         * the Functionality to save, open, open as, new.
         */


        JMenuItem New = new JMenuItem(" New");
        New.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int reply = JOptionPane.showConfirmDialog(null, "Do you want to save", "Save Changes?", JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {

                    JFileChooser fileChooser = new JFileChooser();//     an api to open file directory as a gui
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//only files can be selected
                    int choice = fileChooser.showOpenDialog(null);

                    if(choice == fileChooser.APPROVE_OPTION){
                        String name = JOptionPane.showInputDialog("Enter file name");//enter name.
                        Main.fileName=fileChooser.getSelectedFile().getAbsolutePath()+"/"+name;
                        File file = new File(Main.fileName);
                        /**
                         * Use an output stream to save the StyledDocument object as a file
                         * so that it can be opened later.
                         */
                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(Main.fileName);
                            ObjectOutputStream objectOut = new ObjectOutputStream(fileOutputStream);
                            StyledDocument doc = (StyledDocument) Main.textArea.getDocument();

                            objectOut.writeObject(doc);
                            objectOut.close();
                            System.out.println("The Object  was succesfully written to a file");
                        } catch (FileNotFoundException e1) {
                            //e1.printStackTrace();
                        } catch (IOException e1) {
                            //e1.printStackTrace();
                        }
                    }
                }

                Main.textArea.setText("");
            }
        });


        JMenuItem Save = new JMenuItem("Save");
        Save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Main.fileName==""){
                    JFileChooser fileChooser = new JFileChooser();//to show directories as a files.
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//only files can be selected.
                    int choice = fileChooser.showOpenDialog(null);

                    if(choice == fileChooser.APPROVE_OPTION){
                        String name = JOptionPane.showInputDialog("Enter file name");
                        Main.fileName=fileChooser.getSelectedFile().getAbsolutePath()+"/"+name;
                        System.out.println(Main.fileName);
                        File file = new File(Main.fileName);
                        /**
                         * Use an output stream to save the StyledDocument object as a file
                         * so that it can be opened later.
                         */
                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(Main.fileName);
                            ObjectOutputStream objectOut = new ObjectOutputStream(fileOutputStream);
                            StyledDocument doc = (StyledDocument) Main.textArea.getDocument();

                            objectOut.writeObject(doc);
                            objectOut.close();
                            System.out.println("The Object  was succesfully written to a file");
                        } catch (FileNotFoundException e1) {
                            //e1.printStackTrace();
                        } catch (IOException e1) {
                            //e1.printStackTrace();
                        }
                    }
                }
                else {
                    File file = new File(Main.fileName);
                    /**
                     * Use an output stream to save the StyledDocument object as a file
                     * so that it can be opened later.
                     */
                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(Main.fileName);
                        ObjectOutputStream objectOut = new ObjectOutputStream(fileOutputStream);
                        StyledDocument doc = (StyledDocument) Main.textArea.getDocument();

                        objectOut.writeObject(doc);
                        objectOut.close();
                        System.out.println("The Object  was succesfully written to a file");

                    } catch (FileNotFoundException e1) {
                        //e1.printStackTrace();
                    } catch (IOException e1) {
                        //e1.printStackTrace();
                    }
                }
            }
        });
        JMenuItem SaveAs = new JMenuItem("Save as");
        SaveAs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);// choose only directories
                int choice = fileChooser.showOpenDialog(null);
                if(choice == fileChooser.APPROVE_OPTION){
                    String name = JOptionPane.showInputDialog("Enter file name");
                    Main.fileName=fileChooser.getSelectedFile().getAbsolutePath()+"/"+name;
                    File file = new File(Main.fileName);
                    /**
                     * Use an output stream to save the StyledDocument object as a file
                     * so that it can be opened later.
                     */
                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(Main.fileName);
                        ObjectOutputStream objectOut = new ObjectOutputStream(fileOutputStream);
                        StyledDocument doc = (StyledDocument) Main.textArea.getDocument();

                        objectOut.writeObject(doc);
                        objectOut.close();
                        System.out.println("The Object  was succesfully written to a file");
                    } catch (FileNotFoundException e1) {
                        //e1.printStackTrace();
                    } catch (IOException e1) {
                        //e1.printStackTrace();
                    }
                }
            }
        });

        JMenuItem Open = new JMenuItem("Open");
        Open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int choice = fileChooser.showOpenDialog(null);
                if(choice == fileChooser.APPROVE_OPTION){ //only files can be selected
                    File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                    Main.fileName=fileChooser.getSelectedFile().getAbsolutePath();
                    /**
                     * Use an output stream to open the StyledDocument object file which was stored
                     * previously.
                     */
                    try {
                        Scanner scanner = new Scanner(file);
                        FileInputStream fileIn = new FileInputStream(Main.fileName);
                        ObjectInputStream objectIn = new ObjectInputStream(fileIn);
                        Object obj = objectIn.readObject();
                        System.out.println("The Object has been read from the file");
                        objectIn.close();
                        StyledDocument doc = (StyledDocument)obj;
                        Main.textArea.setDocument(doc);


                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (ClassNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        JMenuItem Cut = new JMenuItem("Cut");
        JMenuItem Copy = new JMenuItem("Copy");
        JMenuItem Paste = new JMenuItem("Paste");
        JMenuItem SelectAll = new JMenuItem("Select all");

        JMenuItem About = new JMenuItem("About");
        JMenuItem Support = new JMenuItem("Support");

        SelectAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.textArea.selectAll();
            }
        });

        /**
         * Add all the stuff created above.
         */
        New.setIcon(iconNew);
        Open.setIcon(iconOpen);
        Save.setIcon(iconSave);
        SaveAs.setIcon(iconSave);

        Cut.setIcon(iconCut);
        Copy.setIcon(iconCopy);
        Paste.setIcon(iconPaste);
        SelectAll.setIcon(iconSelect);

        Find.setIcon(find);
        FindReplace.setIcon(findall);
        ReplaceAll.setIcon(replaceall);


        Open.addActionListener(this);
        Cut.addActionListener(this);
        Copy.addActionListener(this);
        Paste.addActionListener(this);

        About.addActionListener(this);
        Support.addActionListener(this);

        File.add(New);
        File.add(Open);
        File.add(Save);
        File.add(SaveAs);

        Format.add(Lowercase);
        Format.add(Uppercase);

        Edit.add(Cut);
        Edit.add(Copy);
        Edit.add(Paste);
        Edit.add(SelectAll);

        Help.add(About);
        Help.add(Support);

        menuBar.add(File);
        menuBar.add(Edit);

        menuBar.add(Format);
        menuBar.add(Insert);
        menuBar.add(Help);

        Edit.add(Find);
        Edit.add(FindReplace);
        Edit.add(ReplaceAll);


        add(menuBar);
        add(formatBar);
        /**
         * all of these three action listeners take in a word to find or replace and calls their respective functions which are below
         */
        Find.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String find = JOptionPane.showInputDialog(null,"Enter Word: ");

                findd(find);
            }
        });
        FindReplace.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                String find = JOptionPane.showInputDialog(null,"Enter Word: ");
                String newWord = JOptionPane.showInputDialog(null,"Enter Word: ");
                FindAndReplacee(find,newWord);
            }
        });
        ReplaceAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                String find = JOptionPane.showInputDialog(null,"Enter Word: ");
                String newWord = JOptionPane.showInputDialog(null,"Enter Word: ");
                ReplaceAll(find,newWord);
            }
        });

    }

    void CreateFile () throws Exception{                     //this function creates a file and stores the text in textArea
        String fileName = new String();
        fileName = JOptionPane.showInputDialog("Enter the name for the text field");
        File file = new File(fileName);
        PrintWriter input = new PrintWriter(file);
        input.write(Main.textArea.getText());
        input.close();
    }
    private static int findd(String toFind) {
        int start;
        int opt=1;
        toFind = toFind.toLowerCase();
        do{
            String editorText = Main.textArea.getText().toLowerCase();  //extracts the text from the textarea
            start = editorText.indexOf(toFind,Main.index);             //start marks the index of word that has to be found
            Main.index=start+1;                                        //increments the index to find the next word
            if (start != -1) {
                Main.textArea.setCaretPosition(start);                //selects the word from start
                Main.textArea.moveCaretPosition(start + toFind.length()); //to the length of the word
                Main.textArea.getCaret().setSelectionVisible(true);       //shows selection
            }
            else{
                JOptionPane.showMessageDialog(null, "Not Found", "Find-Dialog", start,null); //else shows not found
                return -1;
            }

            opt=JOptionPane.showConfirmDialog(null, "Press Yes to find more");     //goes on till nothing is found and usr says yes
        }while(opt==JOptionPane.YES_OPTION);
        return start;
    }
    private static void FindAndReplacee(String toFind,String New) {
        toFind = toFind.toLowerCase();
        String editorText = Main.textArea.getText().toLowerCase();   //same functionality as findd function
        int start = editorText.indexOf(toFind,Main.index);
        if(start==-1)
            JOptionPane.showMessageDialog(null, "Not Found");
        else
            Main.textArea.setText(editorText.replaceFirst(toFind,New));  //replaces the first instance of the word using built in function of jtextpane
    }
    private static void ReplaceAll(String toFind,String New){
        toFind = toFind.toLowerCase();
        String editorText = Main.textArea.getText().toLowerCase();  //same functionality as findd function
        int start = editorText.indexOf(toFind,Main.index);
        if(start==-1)
            JOptionPane.showMessageDialog(null, "Not Found");
        else
            Main.textArea.setText(editorText.replaceAll(toFind,New));  //replaces every instance of the word using built in function of jtextpane
    }
    @Override
    public void actionPerformed(ActionEvent e) {               //common action listener for a few function as to remove complexity
        if(e.getActionCommand().equalsIgnoreCase("Cut")){
            Main.textArea.cut();  //built in cut function of textarea
        }
        if(e.getActionCommand().equalsIgnoreCase("Copy")){
            Main.textArea.copy();   //built in copy function of textarea
        }
        if(e.getActionCommand().equalsIgnoreCase("Paste")){
            Main.textArea.paste();   //built in paste function of textarea
        }
        if(e.getActionCommand().equalsIgnoreCase("About")){
            HelpFrame obj = new HelpFrame();      //calls helpframe constructor
        }
        if(e.getActionCommand().equalsIgnoreCase("Support")){
            SupportFrame obj = new SupportFrame();  //support frame constructor
        }

        if(e.getActionCommand().equalsIgnoreCase("Oval")){        //check which shape is selected
            Main.Shape="Oval";
            Main.displayShapes.repaint();
        }
        if(e.getActionCommand().equalsIgnoreCase("triangle")){
            Main.Shape="triangle";
            Main.displayShapes.repaint();
        }
        if(e.getActionCommand().equalsIgnoreCase("quadrilateral")){
            //System.out.println("quadilateral pressed");
            Main.Shape="quadrilateral";
            //System.err.println("\nValue in Shapes is "+Main.Shape+"\nCalling paintComponent...");
            Main.displayShapes.repaint();
        }
        if(e.getActionCommand().equalsIgnoreCase("pentagon")){
            Main.Shape="Pentagon";
            //System.err.println("\nValue in Shapes is "+Main.Shape+"\nCalling paintComponent...");
            Main.displayShapes.repaint();
        }
        if(e.getActionCommand().equalsIgnoreCase("hexagon")){
            Main.Shape="Hexagon";
            Main.displayShapes.repaint();
        }
        if(e.getActionCommand().equalsIgnoreCase("Clean")){
            Main.Shape="Clean";
            Main.displayShapes.repaint();
        }
    }
}


class DisplayShapes extends JPanel implements ActionListener{
    private int xrec=200,yrec=100,s1=75,s2=375,x=0,y=0;
    private int triangleX[]=new int[3];     //Triangle array
    private int triangleY[]=new int[3];

    private int quadX[]=new int[4];          //quadrilateral array
    private int quadY[]=new int[4];

    private int pentagonX[]=new int[5];      //pentagon array
    private int pentagonY[]=new int[5];

    private int hexagonX[]=new int[6];       //hexagon array
    private int hexagonY[]=new int[6];

    private int Oval_RecX[]=new int[4];      //oval points to implement drag and change shape funcionality
    private int Oval_RecY[]=new int[4];
    private int xoval=300,yoval=300;

    Color col = new Color(12);

    public DisplayShapes() {
        /**
         * Function set default coordinates for shapes
         *
         * implements action listener to select color of shapes by creating an object of color class
         *
         * implements mouse listener to use mouse dragged functionality to change the size of the shapes
         */
        JMenuBar color = new JMenuBar();  //color selector
        JMenuItem Red= new JMenuItem("Red");
        JMenuItem Yellow = new JMenuItem("Yellow");
        JMenuItem Black = new JMenuItem("Black");
        JMenuItem Magenta = new JMenuItem("Magenta");
        JMenuItem Blue = new JMenuItem("Blue");
        JMenuItem Gray = new JMenuItem("Gray");
        JMenu SetColor = new JMenu("SetColor");
        SetColor.add(Red);
        SetColor.add(Yellow);
        SetColor.add(Black);
        SetColor.add(Magenta);
        SetColor.add(Blue);
        SetColor.add(Gray);

        color.add(SetColor);
        JPanel p1 = new JPanel(new BorderLayout(0, 0));
        p1.add(color,BorderLayout.SOUTH);
        add(p1);
        Red.addActionListener(this);
        Yellow.addActionListener(this);
        Black.addActionListener(this);
        Blue.addActionListener(this);
        Magenta.addActionListener(this);
        Gray.addActionListener(this);

        triangleX[0]=200; triangleY[0]=350;   //Default coordinates fo triangle
        triangleX[1]=150; triangleY[1]=450;
        triangleX[2]=250; triangleY[2]=450;

        quadX[0]=75;  quadY[0]=300;
        quadX[1]=75;  quadY[1]=375;
        quadX[2]=225; quadY[2]=375;
        quadX[3]=225; quadY[3]=300;

        pentagonX[0]=150; pentagonY[0]=200;  //Default cooordinates for pentagon
        pentagonX[1]=75;  pentagonY[1]=300;
        pentagonX[2]=75;  pentagonY[2]=375;
        pentagonX[3]=225; pentagonY[3]=375;
        pentagonX[4]=225; pentagonY[4]=300;

        hexagonX[0]=150; hexagonY[0]=200;   //Default coordinates for hexagon
        hexagonX[1]=75;  hexagonY[1]=300;
        hexagonX[2]=75;  hexagonY[2]=375;
        hexagonX[3]=150; hexagonY[3]=475;
        hexagonX[4]=225; hexagonY[4]=375;
        hexagonX[5]=225; hexagonY[5]=300;

        Oval_RecX[0]=150; Oval_RecY[0]=200;
        Oval_RecX[1]=200; Oval_RecY[1]=300;  //default corner for oval
        Oval_RecX[2]=220; Oval_RecY[2]=255;
        Oval_RecX[3]=170; Oval_RecY[3]=320;

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                x = e.getX(); //get x and y coordinate from continuosly as the mouse is dragged across the panel
                y = e.getY();
                /*
                The if statements firstly check what is the selected shape and insert

                The change dimension function for the first four shapes works by an precision of +10 to -10  around the corner

                by changing coordinates in every mouse movement , x and y gets assigned to the respective points

                and calls repaint after every change

                */
                if(Main.Shape.equalsIgnoreCase("triangle")){
                    if(((x<=triangleX[0]+10)&&(x >=triangleX[0]-10))&&((y)<=triangleY[0]+10)&&(y>=triangleY[0]-10)){
                        triangleX[0]=x; triangleY[0]=y;
                        repaint();
                    }
                    if(((x<=triangleX[1]+10)&&(x>=triangleX[1]-10))&&((y<=triangleY[1]+10)&&(y>=triangleY[1]-10))){
                        triangleX[1]=x; triangleY[1]=y;
                        repaint();
                    }
                    if(((x<=triangleX[2]+10)&&(x>=triangleX[2]-10))&&((y<=triangleY[2]+10)&&(y>=triangleY[2]-10))){
                        triangleX[2]=x; triangleY[2]=y;
                        repaint();
                    }
                }
                if(Main.Shape.equalsIgnoreCase("pentagon")){
                    if((x<=pentagonX[0]+10)&&(x>=pentagonX[0]-10)&&(y<=pentagonY[0]+10)&&(y>=pentagonY[0]-10)){
                        pentagonX[0]=x; pentagonY[0]=y;
                        repaint();
                    }
                    if((x<=pentagonX[1]+10)&&(x>=pentagonX[1]-10)&&(y<=pentagonY[1]+10)&&(y>=pentagonY[1]-10)){
                        pentagonX[1]=x; pentagonY[1]=y;
                        repaint();
                    }
                    if((x<=pentagonX[2]+10)&&(x>=pentagonX[2]-10)&&(y<=pentagonY[2]+10)&&(y>=pentagonY[2]-10)){
                        pentagonX[2]=x; pentagonY[2]=y;
                        repaint();
                    }
                    if((x<=pentagonX[3]+10)&&(x>=pentagonX[3]-10)&&(y<=pentagonY[3]+10)&&(y>=pentagonY[3]-10)){
                        pentagonX[3]=x; pentagonY[3]=y;
                        repaint();
                    }
                    if((x<=pentagonX[4]+10)&&(x>=pentagonX[4]-10)&&(y<=pentagonY[4]+10)&&(y>=pentagonY[4]-10)){
                        pentagonX[4]=x; pentagonY[4]=y;
                        repaint();
                    }
                }
                if(Main.Shape.equalsIgnoreCase("hexagon")){
                    if((x<=hexagonX[0]+10)&&(x>=hexagonX[0]-10)&&(y<=hexagonY[0]+10)&&(y>=hexagonY[0]-10)){
                        hexagonX[0]=x; hexagonY[0]=y;
                        repaint();
                    }
                    if((x<=hexagonX[1]+10)&&(x>=hexagonX[1]-10)&&(y<=hexagonY[1]+10)&&(y>=hexagonY[1]-10)){
                        hexagonX[1]=x; hexagonY[1]=y;
                        repaint();
                    }
                    if((x<=hexagonX[2]+10)&&(x>=hexagonX[2]-10)&&(y<=hexagonY[2]+10)&&(y>=hexagonY[2]-10)){
                        hexagonX[2]=x; hexagonY[2]=y;
                        repaint();
                    }
                    if((x<=hexagonX[3]+10)&&(x>=hexagonX[3]-10)&&(y<=hexagonY[3]+10)&&(y>=hexagonY[3]-10)){
                        hexagonX[3]=x; hexagonY[3]=y;
                        repaint();
                    }
                    if((x<=hexagonX[4]+10)&&(x>=hexagonX[4]-10)&&(y<=hexagonY[4]+10)&&(y>=hexagonY[4]-10)){
                        hexagonX[4]=x; hexagonY[4]=y;
                        repaint();
                    }
                    if((x<=hexagonX[5]+9)&&(x>=hexagonX[5]-9)&&(y<=hexagonY[5]+9)&&(y>=hexagonY[5]-9)){
                        hexagonX[5]=x; hexagonY[5]=y;
                        repaint();
                    }
                }
                if(Main.Shape.equalsIgnoreCase("oval")){

                    if(x>=(Oval_RecX[0]-15)&&y>=(Oval_RecY[0]-15)){
                        Oval_RecX[0]=x;
                        Oval_RecY[0]=y;
                    }
                    else if(x>=(Oval_RecX[1]-15)&&y<=(Oval_RecY[1]+15)){
                        Oval_RecX[1]=x;
                        Oval_RecY[1]=y;
                    }
                    else if(x>=(Oval_RecX[2]-15)&&y<=(Oval_RecY[2]+15)){
                        Oval_RecX[2]=x;
                        Oval_RecY[2]=y;
                    }

                    else if(x>=(Oval_RecX[3]-15)&&y<=(Oval_RecY[3]+15)){
                        Oval_RecX[3]=x;
                        Oval_RecY[3]=y;
                    }
                    repaint();

                }
                if(Main.Shape.equalsIgnoreCase("quadrilateral")){
                    if((x<=quadX[0]+9)&&(x>=quadX[0]-9)&&(y<=quadY[0]+9)&&(y>=quadY[0]-9)){
                        quadX[0]=x; quadY[0]=y;
                        repaint();
                    }
                    if((x<=quadX[1]+9)&&(x>=quadX[1]-9)&&(y<=quadY[1]+9)&&(y>=quadY[1]-9)){
                        quadX[1]=x; quadY[1]=y;
                        repaint();
                    }
                    if((x<=quadX[2]+9)&&(x>=quadX[2]-9)&&(y<=quadY[2]+9)&&(y>=quadY[2]-9)){
                        quadX[2]=x; quadY[2]=y;
                        repaint();
                    }
                    if((x<=quadX[3]+9)&&(x>=quadX[3]-9)&&(y<=quadY[3]+9)&&(y>=quadY[3]-9)){
                        quadX[3]=x; quadY[3]=y;
                        repaint();
                    }
                }
            }
        });
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);           ///basic drawing of shapes with default values as specified in the constructor
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.black, 10));
        g.setColor(col);
        if(Main.Shape.equalsIgnoreCase("quadrilateral")){
            g.fillPolygon(quadX, quadY, 4);
        }
        if(Main.Shape.equalsIgnoreCase("oval")){
            xoval = (int)(Math.sqrt(Math.pow(Oval_RecX[1]-Oval_RecX[2],2)+Math.pow(Oval_RecY[1]-Oval_RecY[2],2)));
            yoval = (int)(Math.sqrt(Math.pow(Oval_RecX[1]-Oval_RecX[0],2)+Math.pow(Oval_RecY[1]-Oval_RecY[0],2)));
            g.fillOval(Oval_RecX[1], Oval_RecY[1], xoval, yoval);
            g.drawString("o", Oval_RecX[1], Oval_RecY[1]);
        }
        if(Main.Shape.equalsIgnoreCase("pentagon")){
            g.fillPolygon(pentagonX, pentagonY, 5);
        }
        if(Main.Shape.equalsIgnoreCase("hexagon")){
            g.fillPolygon(hexagonX, hexagonY, 6);
        }
        if(Main.Shape.equalsIgnoreCase("triangle")){
            g.fillPolygon(triangleX, triangleY, 3);
        }

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equalsIgnoreCase("Red")){ //set selected color as described above
            col=Color.RED;
        }
        if(e.getActionCommand().equalsIgnoreCase("Gray")){
            col=Color.GRAY;
        }
        if(e.getActionCommand().equalsIgnoreCase("yellow")){
            col=Color.yellow;
        }
        if(e.getActionCommand().equalsIgnoreCase("Black")){
            col=Color.black;
        }
        if(e.getActionCommand().equalsIgnoreCase("Blue")){
            col=Color.BLUE;
        }
        if(e.getActionCommand().equalsIgnoreCase("Magenta")){
            col=Color.magenta;
        }
    }
}
/**
 * This class sets all the attributes required for the TextPane like
 * making it scrollable,
 * snd setting its size.
 */
class DisplayField extends JPanel{

    DisplayField(){
        Main.textArea.setPreferredSize(new Dimension(900,900));
        JScrollPane scrollPane = new JScrollPane(Main.textArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(scrollPane);
    }
}

/**
 * a help frame which opens up
 * and has additional information about the authors of this authors.
 */

class HelpFrame extends JFrame{

    public HelpFrame() {
        setTitle("ReadMe");
        Font font = new Font("Jokerman", Font.PLAIN, 35);
        JLabel text = new JLabel("Developed by Dev & Jackson");
        text.setFont(font);
        setSize(600,630);
        add(text);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}

/**
 * a support frame which gives information about the Text Editor
 */
class SupportFrame extends JFrame{

    public SupportFrame() throws HeadlessException {
        setTitle("Support");
        Font font = new Font("Jokerman", Font.PLAIN, 35);
        JLabel text = new JLabel("Text Editor- beta phase");
        text.setFont(font);
        setSize(600,630);
        add(text);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
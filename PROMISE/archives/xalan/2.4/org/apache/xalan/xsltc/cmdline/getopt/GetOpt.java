package org.apache.xalan.xsltc.cmdline.getopt; 

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.List;

import org.apache.xalan.xsltc.cmdline.getopt.IllegalArgumentException;
import org.apache.xalan.xsltc.cmdline.getopt.MissingOptArgException;


/**
* GetOpt is a Java equivalent to the C getopt() library function
* discussed in man page getopt(3C). It provides command line
* parsing for Java applications. It supports the most rules of the
* command line standard (see man page intro(1)) including stacked
* options such as '-sxm' (which is equivalent to -s -x -m); it
* handles special '--' option that signifies the end of options.
* Additionally this implementation of getopt will check for
* mandatory arguments to options such as in the case of
* '-d <file>' it will throw a MissingOptArgException if the 
* option argument '<file>' is not included on the commandline.
* getopt(3C) does not check for this. 
*/
public class GetOpt{
    public GetOpt(String[] args, String optString){
	theOptions = new ArrayList();		 
	int currOptIndex = 0; 
	theCmdArgs = new ArrayList(); 
	theOptionMatcher = new OptionMatcher(optString);
	for(int i=0; i<args.length; i++){
	    String token = args[i];
	    int tokenLength = token.length();
	    }
	    else if(token.startsWith("-") && tokenLength == 2){ 
		theOptions.add(new Option(token.charAt(1)));	
	    }
	    else if(token.startsWith("-") && tokenLength > 2){
		for(int j=1; j<tokenLength; j++){
		    theOptions.add(new Option(token.charAt(j)));
		}
	    }		
	    else if(!token.startsWith("-")){
		if(theOptions.size() == 0){
		    currOptIndex = i;
		}
		else {
		    int indexoflast=0;
		    indexoflast = theOptions.size()-1;
		    Option op = (Option)theOptions.get(indexoflast);
		    char opLetter = op.getArgLetter();
		    if(!op.hasArg() && theOptionMatcher.hasArg(opLetter)){
		        op.setArg(token);
		    }
		    else{
		        currOptIndex = i;
		    }
	  	}

	theOptionsIterator = theOptions.listIterator();

	for(int i=currOptIndex; i<args.length; i++){
	    String token = args[i];
	    theCmdArgs.add(token);
	}
    }


    /**
    * debugging routine to print out all options collected
    */
    public void printOptions(){
	for(ListIterator it=theOptions.listIterator(); it.hasNext();){
	    Option opt = (Option)it.next();
	    System.out.print("OPT =" + opt.getArgLetter());
	    String arg = opt.getArgument();
	    if(arg != null){
	       System.out.print(" " + arg);
	    }
	    System.out.println();
	}
    }

    /**
    * gets the next option found in the commandline. Distinguishes
    * between two bad cases, one case is when an illegal option
    * is found, and then other case is when an option takes an
    * argument but no argument was found for that option.
    * If the option found was not declared in the optString, then 
    * an IllegalArgumentException will be thrown (case 1). 
    * If the next option found has been declared to take an argument, 
    * and no such argument exists, then a MissingOptArgException
    * is thrown (case 2).
    * @param none
    * @return int - the next option found.
    * @throws IllegalArgumentException, MissingOptArgException. 
    */
    public int getNextOption() throws IllegalArgumentException, 
	MissingOptArgException
    {
	int retval = -1;
	if(theOptionsIterator.hasNext()){
	    theCurrentOption = (Option)theOptionsIterator.next();
	    char c = theCurrentOption.getArgLetter();
	    boolean shouldHaveArg = theOptionMatcher.hasArg(c);
	    String arg = theCurrentOption.getArgument();
	    if(!theOptionMatcher.match(c)){
		throw (new IllegalArgumentException("Option " +
			c + " is not valid."));
	    }
	    else if(shouldHaveArg && (arg == null)){
		throw (new MissingOptArgException("Option " + 
			c + " is missing its argument."));
	    }
	    retval = c;
	}
	return retval;
    }

    /**
    * gets the argument for the current parsed option. For example,
    * in case of '-d <file>', if current option parsed is 'd' then
    * getOptionArg() would return '<file>'.
    * @param none
    * @return String - argument for current parsed option.
    */
    public String getOptionArg(){
	String retval = null;
	String tmp = theCurrentOption.getArgument();
	char c = theCurrentOption.getArgLetter();
	if(theOptionMatcher.hasArg(c)){
	    retval = tmp;
	}
	return retval;	
    }

    /**
    * gets list of the commandline arguments. For example, in command
    * such as 'cmd -s -d file file2 file3 file4'  with the usage
    * 'cmd [-s] [-d <file>] <file>...', getCmdArgs() would return
    * the list {file2, file3, file4}.
    * @params none
    * @return String[] - list of command arguments that may appear
    *                    after options and option arguments.
    */
    public String[] getCmdArgs(){
	String[] retval = new String[theCmdArgs.size()];
	int i=0;
        for(ListIterator it=theCmdArgs.listIterator(); it.hasNext();){
            retval[i++] = (String)it.next();
        }
	return retval;
    }


    private Option theCurrentOption = null;
    private ListIterator theOptionsIterator; 
    private List theOptions = null;
    private List theCmdArgs = null;
    private OptionMatcher theOptionMatcher = null;


    class Option{
        private char theArgLetter;
        private String theArgument = null;
        public Option(char argLetter) { theArgLetter = argLetter; }
        public void setArg(String arg) { 
	    theArgument = arg;
        }
        public boolean hasArg() { return (theArgument != null); } 
        public char getArgLetter() { return theArgLetter; }
        public String getArgument() { return theArgument; }


    class OptionMatcher{
        public OptionMatcher(String optString){
	    theOptString = optString;	
        }
        public boolean match(char c){
	    boolean retval = false;
	    if(theOptString.indexOf(c) != -1){
	        retval = true;
	    }
	    return retval;	
        }
        public boolean hasArg(char c){
	    boolean retval = false;
	    int index = theOptString.indexOf(c)+1; 
	    if (index == theOptString.length()){
	        retval = false;
	    }
            else if(theOptString.charAt(index) == ':'){
                retval = true;
            }
            return retval;
        }
        private String theOptString = null;
    

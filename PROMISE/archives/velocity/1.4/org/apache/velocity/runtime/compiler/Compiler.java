import org.apache.bcel.Constants;

/**
 * The start of a velocity template compiler. Incomplete.
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @version $Id: Compiler.java 75955 2004-03-03 23:23:08Z geirm $
 */
public class Compiler implements InstructionConstants
{
    public static void main(String[] args)
    {
        String template = args[0].substring(0, args[0].indexOf("."));
        ClassGen cg =
                new ClassGen(template, "java.lang.Object", "<generated>",
                Constants.ACC_PUBLIC | Constants.ACC_SUPER, null);
        
        InstructionList il = new InstructionList();
        MethodGen mg = new MethodGen(Constants.ACC_STATIC |
                new ArrayType(Type.STRING, 1)},
                il, cp);


        int br_index = cp.addClass("java.io.BufferedReader");
        int ir_index = cp.addClass("java.io.InputStreamReader");
                "Ljava/io/PrintStream;");
                "Ljava/io/InputStream;");


        il.append(new NEW(br_index));
        il.append(DUP);
        il.append(new NEW(ir_index));
        il.append(DUP);
        il.append(new GETSTATIC(system_in));


        il.append( new INVOKESPECIAL(
                cp.addMethodref("java.io.InputStreamReader", "<init>",
                "(Ljava/io/InputStream;)V")));
        il.append( new INVOKESPECIAL(
                cp.addMethodref("java.io.BufferedReader", "<init>", "(Ljava/io/Reader;)V")));
        

        LocalVariableGen lg = mg.addLocalVariable("in",
                new ObjectType("java.io.BufferedReader"), null, null);
        int in = lg.getIndex();


        lg = mg.addLocalVariable("name", Type.STRING, null, null);
        int name = lg.getIndex();
        il.append(ACONST_NULL);

        InstructionHandle try_start = il.append(new GETSTATIC(system_out));
        il.append(new PUSH(cp, "I will be a template compiler!"));
        il.append( new INVOKEVIRTUAL(
                cp.addMethodref("java.io.PrintStream", "println", "(Ljava/lang/String;)V")));
        

        GOTO g = new GOTO(null);
        InstructionHandle try_end = il.append(g);

        InstructionHandle handler = il.append(RETURN);
        mg.addExceptionHandler(try_start, try_end, handler,
                new ObjectType("java.io.IOException"));


        InstructionHandle ih = il.append(new GETSTATIC(system_out));
        g.setTarget(ih);

        
        il.append(new NEW(cp.addClass("java.lang.StringBuffer")));
        il.append(DUP);
        il.append(new PUSH(cp, " "));
        il.append( new INVOKESPECIAL(
                cp.addMethodref("java.lang.StringBuffer", "<init>", "(Ljava/lang/String;)V")));
        
        il.append(new ALOAD(name));


        String sig = Type.getMethodSignature(Type.STRINGBUFFER,
                new Type[]{ Type.STRING });
        il.append( new INVOKEVIRTUAL(
                cp.addMethodref("java.lang.StringBuffer", "append", sig)));

        il.append( new INVOKEVIRTUAL(
                cp.addMethodref("java.lang.StringBuffer", "toString", "()Ljava/lang/String;")));

        il.append(RETURN);
        
        cg.addMethod(mg.getMethod());

        cg.addEmptyConstructor(Constants.ACC_PUBLIC);

        try
        {
            cg.getJavaClass().dump(template + ".class");
        }
        catch (java.io.IOException e)
        {
            System.err.println(e);
        }
    }
}

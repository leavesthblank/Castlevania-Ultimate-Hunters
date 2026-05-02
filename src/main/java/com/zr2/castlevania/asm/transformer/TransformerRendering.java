package com.zr2.castlevania.asm.transformer;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class TransformerRendering implements IClassTransformer {

    private static final String MCENTITYRENDERER_NAME = "net.minecraft.client.renderer.EntityRenderer";

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (MCENTITYRENDERER_NAME.equals(name) || MCENTITYRENDERER_NAME.equals(transformedName)) {
            return patchRenderer(basicClass);
        }
        return basicClass;
    }

    private byte[] patchRenderer(byte[] basicClass) {
        boolean isFinished = false;

        System.out.println("Applying ASM to MinecraftServer...");

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);

        for (MethodNode method : classNode.methods) {
            if (method.desc.equals("(F)V") && method.name.equals("orientCamera") || method.name.equals("func_78467_g") || method.name.equals("h")) {
                InsnList list = new InsnList();
                for (AbstractInsnNode node : method.instructions.toArray()) {
                    list.add(node);
                    AbstractInsnNode prevNode = node.getPrevious();
                    if (!isFinished && node.getOpcode() == Opcodes.GETFIELD && prevNode != null && prevNode.getOpcode() == Opcodes.ALOAD && ((VarInsnNode) prevNode).var == 2) {
                        MethodInsnNode insnNode = new MethodInsnNode(Opcodes.INVOKESTATIC,
                                "com/zr2/castlevania/event/client/ClientStoneAbilityEventHandler",
                                "orientCameraHook",
                                "()F",
                                false);
                        list.add(insnNode);
                        list.add(new InsnNode(Opcodes.FADD));
                        isFinished = true;
                    }
                }

                method.instructions.clear();
                method.instructions.add(list);
                System.out.println("Done!");
                isFinished = true;
            }
        }

        if (!isFinished) {
            System.err.println("ASM transformation did not execute! Printing all functions for debug.");
            for (MethodNode method : classNode.methods) {
                System.out.println(method.name + " " + method.desc);
            }
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);
        return writer.toByteArray();
    }

}

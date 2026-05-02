package com.zr2.castlevania.asm.transformer;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class TransformerTickRate implements IClassTransformer {

    private static final String MCSERVER_CLASS = "net.minecraft.server.MinecraftServer";
    private static final String MCWORLD_CLASSS = "net.minecraft.world.World";
    private static final String TICK_EVENT_CLASS = "com/zr2/castlevania/event/server/ServerTickEventHandler";

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (basicClass == null) return null;

        try {
            if (name.equals(MCSERVER_CLASS) || transformedName.equals(MCSERVER_CLASS)) {
                return patchServer(basicClass);
            } else if (transformedName.equals(MCWORLD_CLASSS) || name.equals(MCWORLD_CLASSS)) {
                return patchWorld(basicClass);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return basicClass;
    }


    private byte[] patchServer(byte[] bytes) {
        boolean isFinished = false;

        System.out.println("Applying ASM to MinecraftServer...");

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);

        for (MethodNode method : classNode.methods) {
            if (method.desc.equals("()V") && method.name.equals("updateTimeLightAndEntities") || method.name.equals("func_71190_q") || method.name.equals("v")) {
                InsnList list = new InsnList();
                for (AbstractInsnNode node : method.instructions.toArray()) {
                    if (node.getOpcode() == Opcodes.ASTORE && ((VarInsnNode) node).var == 2) {
                        MethodInsnNode insnNode = (MethodInsnNode) node.getPrevious();
                        insnNode.owner = TICK_EVENT_CLASS;
                        insnNode.name = "getWorldIDs";
                    }
                    list.add(node);
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

    private byte[] patchWorld(byte[] bytes) {
        boolean isFinished = false;

        System.out.println("Applying ASM to World...");

        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);

        for (MethodNode method : classNode.methods) {
            if ((method.desc.equals("(Lnet/minecraft/entity/Entity;Z)V") || method.desc.equals("(Lsa;Z)V")) &&
                    (method.name.equals("updateEntityWithOptionalForce") || method.name.equals("func_72866_a") || method.name.equals("a"))) {
                InsnList list = new InsnList();
                list.add(new VarInsnNode(Opcodes.ALOAD, 1));
                list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, TICK_EVENT_CLASS, "onEntityUpdate", "(Ljava/lang/Object;)V", false));
                method.instructions.insert(list);

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

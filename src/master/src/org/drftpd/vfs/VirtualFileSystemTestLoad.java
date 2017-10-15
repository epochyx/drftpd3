package org.drftpd.vfs;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

public class VirtualFileSystemTestLoad {

	@Test
	public void test() {
		System.out.println("Test");
	}

	public int getFiles(VirtualFileSystemDirectory dir) {
		int count = 0;
		boolean write = false;
		try {
			if(write) dir.writeToDisk();
			for (String file : dir.getInodeNames()) {
				VirtualFileSystemInode inode;

				inode = dir.getInodeByName(file);
				if(inode.isLink()) System.out.println("Found inode " + inode);
				if(write) inode.writeToDisk();
				count++;
				if (inode.isDirectory()) {
					count += getFiles((VirtualFileSystemDirectory) inode);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return count;

	}

	@Test
	public void testLoadInode() {
		System.out.println("Testing Loading");
		VirtualFileSystem vfs = VirtualFileSystem.getVirtualFileSystem();
		int count = 0;
		count = getFiles(vfs.getRoot());
		System.out.println("Manipulated " + count + " files");
	}

}

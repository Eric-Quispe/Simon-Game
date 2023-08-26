package cop2805;

import java.awt.Button;
import java.util.concurrent.CountDownLatch;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import java.util.Random;
import javax.swing.JButton;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
// below is what is needed for sound 
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class Simon extends Thread
{
 private LinkedList<String> simonsOrder = new LinkedList<>();// to keep the element in order we will use linkedList
 
 private LinkedList<String> usersOrder = new LinkedList<>();
 
 private boolean correctOrder =true;// used to see if the game should continue
 

 private JFrame gameWindow = new JFrame("Simon Says!");
 private JButton btnNewButton = new JButton("");
 private JButton btnNewButton_1 = new JButton("");
 private JButton btnNewButton_2 = new JButton("");
 private JButton btnNewButton_3 = new JButton("");
 private JButton startButton = new JButton("Start");
 private int counter=0;
 private JLabel lblNewLabel_1 = new JLabel("Current Level: "+this.counter);

 // this is how to make a HashMap with the values entered at the same time
 private HashMap<Integer,String> numbersAndColors;
 
 private Random keyValueToGenerateColor = new Random();
 private final int lowerBound=1;
 private final int upperBound=4;
 private CountDownLatch buttonLatch; // Add CountDownLatch
 
 

 
 public Simon() 
 {
	 numbersAndColors = new HashMap<>();
     numbersAndColors.put(1, "green");
     numbersAndColors.put(2, "red");
     numbersAndColors.put(3, "yellow");
     numbersAndColors.put(4, "blue");
     gui();
    
 }
 
 public void recursiveMethod(LinkedList<String> simonsOrder,LinkedList<String> usersOrder) // to repeat Simons order if users input is matchs
 {   
	 this.counter++;
	 lblNewLabel_1.setText("Current Level: " + counter);
	 generateSimonsOrder();
	 colorChangesBasedOnSimonsOrder();
	  // Compare user's input with Simon's order
	 // Initialize the CountDownLatch with the number of colors in Simon's order
     buttonLatch = new CountDownLatch(simonsOrder.size());
     

     // Compare user's input with Simon's order
     try 
     {
         buttonLatch.await(); // Wait for the user to click the correct number of buttons the countdown decreases everytime a click happen in the action lister of the buttons in GUI
     } catch (InterruptedException e) 
     {
         e.printStackTrace();
     }
     
     try {
		Thread.sleep(1000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
	    for (int i = 0; i < usersOrder.size(); i++) 
	    {
	        String simonColor = simonsOrder.get(i);
	        String userColor = usersOrder.get(i);

	        if (!simonColor.equals(userColor)) {
	            this.correctOrder = false;
	            break;
	        }
	    }

	    if (correctOrder) {
	        // Continue the game
	        usersOrder.clear();
	        
	        lblNewLabel_1.setText("Current Level: " + counter);
	        recursiveMethod(simonsOrder, usersOrder);
	    }
	 
	 else 
	 {
		simonsOrder.clear();
		usersOrder.clear();
		this.correctOrder = true;
		this.counter=0;
		gameOver();
		lblNewLabel_1.setText("Current Level: " + counter);
	 }
	 
 }
 
 
 public void gameOver() 
 {
	 
	 Thread flashRedThreadEnd = new Thread(new flashRedRunnable());
	 Thread flashGreenThreadEnd = new Thread(new flashGreenRunnable());
	 Thread flashBlueThreadEnd = new Thread(new flashBlueRunnable());
	 Thread flashYellowThread = new Thread(new flashYellowRunnable());
	 flashYellowThread.start();
	 flashBlueThreadEnd.start();
	 flashRedThreadEnd.start();
	 flashGreenThreadEnd.start();
	 try {
         // Load the sound file
         File soundFile = new File("/Users/unknownzodiac/Documents/soundEffects/gameOverSound.wav");
         Clip clip = AudioSystem.getClip();
         clip.open(AudioSystem.getAudioInputStream(soundFile));

         // Play the sound
         clip.start();

         // Wait for the sound to finish
         while (!clip.isRunning())
             Thread.sleep(10);
         while (clip.isRunning())
             Thread.sleep(10);

         // Close the clip
         clip.close();
     } 
	 catch (IOException | LineUnavailableException | UnsupportedAudioFileException | InterruptedException e) 
	 {
         e.printStackTrace();
     }
	 
	 
 }
 
 public class startGame implements Runnable
 {
	 public void run() 
	 {
		 recursiveMethod(simonsOrder,usersOrder);
	 }
 }
 
 
 public class GreenRunnable implements Runnable
 {
  private Object lock = new Object(); // Lock object for synchronization

	@Override
	public void run() 
	{
		//btnNewButton_1.setBackground(new Color(0,0,0));
		btnNewButton_1.setBackground(new Color(70, 255, 0));
		
		   try {
	            Thread.sleep(700); // Wait for 1 second
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
		   btnNewButton_1.setBackground(new Color(50, 109, 35));
		   try {
	            Thread.sleep(300); // Wait for 1 second
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
		   synchronized (lock) {
	            lock.notify();
	        }
		
	}
	 
 }// end of inner class Green Runnable
 public class flashGreenRunnable implements Runnable
 {
  private Object lock = new Object(); // Lock object for synchronization

	@Override
	public void run() 
	{
		//btnNewButton_1.setBackground(new Color(0,0,0));
		btnNewButton_1.setBackground(new Color(70, 255, 0));
		
		   try {
	            Thread.sleep(400); // Wait for 1 second
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
		   btnNewButton_1.setBackground(new Color(50, 109, 35));
		   
	}
	 
 }// end of inner class flash Green Runnable
 
 public class RedRunnable implements Runnable
 {
	private Object lock = new Object(); // Lock object for synchronization

	@Override
	public void run() 
	{
		//btnNewButton.setBackground(null);
		btnNewButton.setBackground(new Color(238,75,43));
		try {
            Thread.sleep(700); // Wait for 1 second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		btnNewButton.setBackground(new Color(127, 34, 29));
		try {
            Thread.sleep(300); // Wait for 1 second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	   
	   synchronized (lock) {
            lock.notify();
        }
		
	}
	 
 }// end of inner class Red Runnable
 
 public class flashRedRunnable implements Runnable
 {
	private Object lock = new Object(); // Lock object for synchronization

	@Override
	public void run() 
	{
		//btnNewButton.setBackground(null);
		btnNewButton.setBackground(new Color(238,75,43));
		try {
            Thread.sleep(400); // Wait for 1 second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		btnNewButton.setBackground(new Color(127, 34, 29));
	
	}
	 
 }// end of inner class flash Red Runnable

 
 public class YellowRunnable implements Runnable
 {
	private Object lock = new Object(); // Lock object for synchronization

	@Override
	public void run() 
	{
		btnNewButton_3.setBackground(null);
		btnNewButton_3.setBackground(new Color(255,234,0));
		try {
            Thread.sleep(700); // Wait for 1 second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		  
	    btnNewButton_3.setBackground(new Color(154, 149, 25));
	    try {
            Thread.sleep(300); // Wait for 1 second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	   
	   synchronized (lock) {
            lock.notify();
        }
		
	}
	 
 }// end of inner class Yellow Runnable
 
 public class flashYellowRunnable implements Runnable
 {
	private Object lock = new Object(); // Lock object for synchronization

	@Override
	public void run() 
	{
		btnNewButton_3.setBackground(null);
		btnNewButton_3.setBackground(new Color(255,234,0));
		try {
            Thread.sleep(400); // Wait for 1 second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		  
	    btnNewButton_3.setBackground(new Color(154, 149, 25));
	
	}
	 
 }// end of inner class flash Yellow Runnable
 
 public class BlueRunnable implements Runnable
 {
	private Object lock = new Object(); // Lock object for synchronization
	@Override
	public void run() 
	{
		btnNewButton_2.setBackground(null);
		btnNewButton_2.setBackground(new Color(0,150,255));
		try {
            Thread.sleep(700); // Wait for 1 second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		 btnNewButton_2.setBackground(new Color(19, 21, 153));
		 try {
	            Thread.sleep(300); // Wait for 1 second
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	   
	   synchronized (lock) {
            lock.notify();
        }
		
	}
	 
 }// end of inner class Blue Runnable
 
 public class flashBlueRunnable implements Runnable
 {
	private Object lock = new Object(); // Lock object for synchronization
	@Override
	public void run() 
	{
		btnNewButton_2.setBackground(null);
		btnNewButton_2.setBackground(new Color(0,150,255));
		try {
            Thread.sleep(400); // Wait for 1 second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		 btnNewButton_2.setBackground(new Color(19, 21, 153));
	}
	 
 }// end of inner class flash Blue Runnable

 
 
 public void gui() 
 {
	 gameWindow.getContentPane().setForeground(new Color(0, 0, 0));
	 gameWindow.getContentPane().setBackground(new Color(0, 0, 0));
	 //JFrame.setDefaultLookAndFeelDecorated(true);
	
	 this.gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 
	 this.gameWindow.setSize(600,600);
	 Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth =  screenSize.width;
		int screenHeight = screenSize.height;
		int frameWidth = this.gameWindow.getWidth();
		int frameHeight = this.gameWindow.getHeight();
		int x= (screenWidth-frameWidth)/2;
		int y =(screenHeight-frameHeight)/2;
		
		
		this.gameWindow.setLocation(x, y);
		gameWindow.getContentPane().setLayout(null);
		
		//JButton btnNewButton = new JButton("Red");
		btnNewButton.setBackground(new Color(127, 34, 29));
		btnNewButton.setOpaque(true);
		btnNewButton.setBorderPainted(false);
		btnNewButton.setBounds(326, 97, 179, 147);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				Thread flashRedThread = new Thread(new flashRedRunnable());
				flashRedThread.start();
				usersOrder.add("red");
				 buttonLatch.countDown(); // Notify the latch on button click
			}
		});
		gameWindow.getContentPane().add(btnNewButton);
		
		//JButton btnNewButton_1 = new JButton("Green");
		btnNewButton_1.setBorderPainted(false);
		btnNewButton_1.setOpaque(true);
		btnNewButton_1.setBackground(new Color(50, 109, 35));
		btnNewButton_1.setBounds(104, 97, 179, 147);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{   
				Thread flashGreenThread = new Thread(new flashGreenRunnable());
				flashGreenThread.start();
				usersOrder.add("green");
				buttonLatch.countDown(); // Notify the latch on button click
			}
		});
		gameWindow.getContentPane().add(btnNewButton_1);
		
		//JButton btnNewButton_2 = new JButton("Blue");
		btnNewButton_2.setBackground(new Color(19, 21, 153));
		btnNewButton_2.setBorderPainted(false);
		btnNewButton_2.setOpaque(true);
		btnNewButton_2.setBounds(326, 351, 179, 138);
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				Thread flashBlueThread = new Thread(new flashBlueRunnable());
				flashBlueThread.start();
				usersOrder.add("blue");
				 buttonLatch.countDown(); // Notify the latch on button click
			}
		});
		gameWindow.getContentPane().add(btnNewButton_2);
		
		//JButton btnNewButton_3 = new JButton("Yellow");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				Thread flashYellowThread = new Thread(new flashYellowRunnable());
				flashYellowThread.start();
				usersOrder.add("yellow");
				buttonLatch.countDown(); // Notify the latch on button click
			}
		});
		btnNewButton_3.setBorderPainted(false);
		btnNewButton_3.setOpaque(true);
		btnNewButton_3.setBackground(new Color(154, 149, 25));
		btnNewButton_3.setBounds(104, 342, 179, 147);
		gameWindow.getContentPane().add(btnNewButton_3);
		
		JLabel lblNewLabel = new JLabel("Simon");
		lblNewLabel.setForeground(new Color(68, 247, 255));
		lblNewLabel.setBounds(283, 276, 47, 26);
		gameWindow.getContentPane().add(lblNewLabel);
		startButton.setOpaque(true);
		startButton.setBackground(new Color(2, 2, 2));
		startButton.setForeground(new Color(2, 2, 2));
		
		
		startButton.setBounds(388, 276, 117, 29);
		gameWindow.getContentPane().add(startButton);
		
		startButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) 
			{
				
				Thread start = new Thread(new startGame());
				start.start();
			}
			});
		lblNewLabel_1.setForeground(new Color(68, 247, 255));
		
		
		lblNewLabel_1.setBounds(104, 281, 143, 16);
		gameWindow.getContentPane().add(lblNewLabel_1);
		 this.gameWindow.setVisible(true);
		
		 // this was created but now the GUI does not load we need to make threads and pauses for the threads
		 
	 
 }// end of GUI method
 
 public void colorChangesBasedOnSimonsOrder() {
	 for(String element: simonsOrder) 
		{
		 
		
			switch (element) 
			{
			case "green":
			Thread greenThread = new Thread(new GreenRunnable());
			greenThread.start();
			 try {
              greenThread.join(); // Wait for the thread to complete
          } catch (InterruptedException e) {
              e.printStackTrace();
          }
			break;
			
			case "red":
				Thread redRunnable = new Thread(new RedRunnable());
				redRunnable.start();
				try {
					redRunnable.join(); // Wait for the thread to complete
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }

				break;
			
			case "yellow":
				Thread yellowRunnable = new Thread(new YellowRunnable());
				yellowRunnable.start();
				 try {
					 yellowRunnable.join(); // Wait for the thread to complete
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
				break;
				
				
			case "blue":
				Thread blueRunnable = new Thread(new BlueRunnable());
				blueRunnable.start();
				 try {
					 blueRunnable.join(); // Wait for the thread to complete
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
				break;
				
			}
		     
			
		 
		}
 }
 
 public void generateSimonsOrder()// this should work to start the order for simon says
 {
	int randomKeyValue = keyValueToGenerateColor.nextInt(upperBound-lowerBound+1)+lowerBound;// note that if 4 is used only the range of numbers this would generate is 0-3 only
	String color = numbersAndColors.get(randomKeyValue);
	simonsOrder.add(color);
	
	
	/* This code what just to see if the colors would be added to the HasHMap
	for(String element: simonsOrder) 
	{
		System.out.println(element);
	}
	*/
	
 }
}// end of class

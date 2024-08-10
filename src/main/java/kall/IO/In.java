package kall.IO;

import kall.Thread.MyBlockingQueue;
import kall.Thread.MyRunnableFactory;
import kall.Thread.MyThreadFactory;
import kall.Thread.MyThreadPool;
import kall.entity.Data;
import kall.reflection.InjectData;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class In {
    /**
     * 测试方法，读指定resource下的dataTest文件
     */
    public void readDataTest() {
        ThreadFactory factory = new MyThreadFactory("readData");
        MyThreadPool executor = new MyThreadPool(4, 4, 1, TimeUnit.MINUTES, new MyBlockingQueue<>(100), factory);

        File targetFile = new File("src/main/resources/dataTest.txt");

        Runnable task1 = new Runnable() {
            @Override
            public void run() {

                try {
                    FileReader reader = new FileReader(targetFile);
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    String line = null;
                    int count = 1;
                    while ((line = bufferedReader.readLine()) != null) {
                        System.out.println(count + ". " + line);
//                        System.out.print(count + " ");
                        count++;
                    }
                    System.out.println();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        /*
        按需读取文件，逐行处理，适合处理大文件，因为它不会将整个文件一次性加载到内存中。
         */
        Runnable task2 = new Runnable() {
            @Override
            public void run() {
                try (Stream<String> stream = Files.lines(Paths.get(targetFile.getPath()))) {
                    stream.forEach(System.out::println);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        /*
        一次性读取整个文件并将其加载到内存中，适合处理小文件或需要一次性访问所有行的场景。
         */
        //暂定使用该方法
        Runnable task3 = new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> lines = Files.readAllLines(Paths.get(targetFile.getPath()));
                    System.out.println("how many lines? --" + lines.size());
                    InjectData injectData = new InjectData();
                    List<Data> dataList = injectData.injectTest(lines);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        MyRunnableFactory runnableFactory = new MyRunnableFactory();


//        executor.execute(task1);
        executor.execute(runnableFactory.recordCostTime(task3));


        try {
//            Thread.sleep(5000);
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(executor.getPoolRunningTime() + "ms");
    }

    /**
     * 项目使用方法：Files.readAllLines，一次性读取整个文件并将其加载到内存中。
     */
    public boolean readData_v0(LinkedBlockingQueue<List<Data>> queue) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("src/main/resources/dataTest.txt"));
//            System.out.println("how many lines? --"+lines.size());
            InjectData injectData = new InjectData();
            int distance = 100000;
            int next = distance;
            int last = 0;
            while (next <= lines.size()) {
//                System.out.println("start in "+next+":  ");
                List<String> block = lines.subList(last, next);
                queue.put(injectData.injectTest(block));
                last = next;
                next += distance;
            }
            queue.put(injectData.injectTest(lines.subList(last, lines.size())));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public boolean readData_v0_1(LinkedBlockingQueue<List<String>> queue) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("src/main/resources/dataTest.txt"));
            int dis = 1000;
            int next = dis;
            int last = 0;
            while (next <= lines.size()) {
                List<String> block = lines.subList(last, next);
                queue.put(block);
                last = next;
                next += dis;
            }
            queue.put(lines.subList(last, lines.size()));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public boolean readData_v1_0(ArrayList<LinkedBlockingQueue<List<String>>> queueList) {
//        long time = System.currentTimeMillis();
        try {
            List<String> lines = Files.readAllLines(Paths.get("src/main/resources/dataTest.txt"));
            System.out.println("how many lines? --" + lines.size());
            int distance = lines.size() / queueList.size();
            int next = distance;
            int last = 0;
            int currentList = queueList.size() - 1;
            while (next <= lines.size()) {
                List<String> block = lines.subList(last, next);
//                System.out.println("currentList is "+currentList);
                queueList.get(currentList).add(block);
//                System.out.println("放进去一个");
                last = next;
                next += distance;
                currentList--;
                if (currentList < 0)
                    currentList = queueList.size() - 1;
            }
            List<String> block = lines.subList(last, lines.size());
            queueList.get(currentList).add(block);
//            System.out.println("放了");
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println("onLyRead:" + (System.currentTimeMillis() - time) + "ms");
        return true;
    }
}

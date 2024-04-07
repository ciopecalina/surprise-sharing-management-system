import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.TimeUnit;

abstract class AbstractGiveSurprises {

    protected IBag bag;
    protected int waitTime;

    public AbstractGiveSurprises(String type, int waitTime) {
        IBagFactory bagFactory = BagFactory.getInstance();
        this.bag = bagFactory.makeBag(type);
        this.waitTime = waitTime;
    }

    public void put(ISurprise newSurprise) {
        bag.put(newSurprise);
    }

    public void put(IBag surprises) {
        bag.put(surprises);
    }

    public void give() {
        ISurprise surprise = bag.takeOut();
        surprise.enjoy();
        giveWithPassion();
        waitTimeInterval(waitTime);
    }

    public void giveAll() {
        while (!isEmpty()) {
            give();
            waitTimeInterval(waitTime);
        }
    }

    private void waitTimeInterval(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isEmpty() {
        return bag.isEmpty();
    }

    abstract void giveWithPassion();
}

class BagFactory implements IBagFactory {

    private static final BagFactory instance = new BagFactory();

    // Private constructor to prevent instantiation
    private BagFactory() {
    }

    public static BagFactory getInstance() {
        return instance;
    }

    @Override
    public IBag makeBag(String type) {
        switch (type) {
            case "LIFO":
                return new BagLIFO();
            case "FIFO":
                return new BagFIFO();
            case "RANDOM":
                return new BagRandom();
            default:
                throw new IllegalArgumentException("Invalid bag type: " + type);
        }
    }
}

class BagFIFO implements IBag {

    private LinkedList<ISurprise> surprises;

    BagFIFO() {
        surprises = new LinkedList<>();
    }

    @Override
    public void put(ISurprise newSurprise) {
        surprises.addLast(newSurprise);
    }

    @Override
    public void put(IBag bagOfSurprises) {
        while (!bagOfSurprises.isEmpty()) {
            ISurprise surprise = bagOfSurprises.takeOut();
            surprises.addLast(surprise);
        }
    }

    @Override
    public ISurprise takeOut() {
        if (!isEmpty()) {
            return surprises.removeFirst();
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
        return surprises.isEmpty();
    }

    @Override
    public int size() {
        return surprises.size();
    }
}

class BagLIFO implements IBag {

    private Stack<ISurprise> surprises;

    BagLIFO() {
        surprises = new Stack<>();
    }

    @Override
    public void put(ISurprise newSurprise) {
        surprises.push(newSurprise);
    }

    @Override
    public void put(IBag bagOfSurprises) {
        while (!bagOfSurprises.isEmpty()) {
            ISurprise surprise = bagOfSurprises.takeOut();
            surprises.push(surprise);
        }
    }

    @Override
    public ISurprise takeOut() {
        if (!surprises.isEmpty()) {
            return surprises.pop();
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
        return surprises.isEmpty();
    }

    @Override
    public int size() {
        return surprises.size();
    }
}

class BagRandom implements IBag {

    private List<ISurprise> surprises;

    BagRandom() {
        surprises = new ArrayList<>();
    }

    @Override
    public void put(ISurprise newSurprise) {
        surprises.add(newSurprise);
    }

    @Override
    public void put(IBag bagOfSurprises) {
        while (!bagOfSurprises.isEmpty()) {
            ISurprise surprise = bagOfSurprises.takeOut();
            surprises.add(surprise);
        }
    }

    @Override
    public ISurprise takeOut() {
        if (!surprises.isEmpty()) {
            Collections.shuffle(surprises);
            ISurprise extractedSurprise = surprises.remove(0);
            return extractedSurprise;
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
        return surprises.isEmpty();
    }

    @Override
    public int size() {
        return surprises.size();
    }
}

class Candies implements ISurprise {

    private static final String[] types = {"chocolate", "jelly", "fruits", "vanilla"};
    private String type;
    private int num;

    public Candies(int num, String type) {
        this.num = num;
        this.type = type;
    }

    @Override
    public void enjoy() {
        System.out.println("You received " + num + " " + type + " candies.");
    }

    @Override
    public String toString() {
        return "[Candies] number = " + num + ", type = " + type;
    }

    public static Candies generate() {
        Random random = new Random();
        int typeIndex = random.nextInt(types.length);
        int num = random.nextInt(10) + 1;

        return new Candies(num, types[typeIndex]);
    }
}

class FortuneCookie implements ISurprise {

    private static final String[] quotes = {
            "When you are content to simply be yourself and not compare yourself to others, everyone will respect you. (Lao Tse)",
            "Once you choose hope, anything is possible. (Christopher Reeve)",
            "I learned silence from the talkative, tolerance from the intolerant and kindness from the wicked. (Khalil Gibran)",
            "He who has no merit always envies the merits of others. (Francis Bacon)",
            "The highest measure of value is actually given during the confrontations to which life subjects you. (Anonymous)",
            "I discovered that if you love life, life will love you. (Arthur Rubinstein)",
            "Every life experience, every lesson I learn is the key to my future. (Clark Gable)",
            "The first condition to be happy is not to have time to think about unhappiness. (George Bernard Shaw)",
            "Give the best you have in you, because everything in your life belongs to you alone. (Ralph Waldo Emerson)",
            "He who lives in his own world is crazy. (Paulo Coelho)",
            "For every evil there are two cures: time and silence. (Alexandre Dumas)",
            "In life things are easy, only the wrong idea that it would be difficult stops us from being extraordinary. (Marian Rujoiu)",
            "Action may not bring you happiness, but there is no happiness without action. (William James)",
            "I never see what has been done. I only see what remains to be done. (Buddha)",
            "Don't waste your time knocking on a wall hoping you'll turn it into a door. (Coco Chanel)",
            "Attach yourself to those who can make you better and welcome those who, in turn, you can make better. (Seneca)",
            "Better to consume than to rust. (Denis Diderot)",
            "Wise is the one who lives every day as if every day and every hour he could die. (Francisco Gomez de Quevedo)",
            "We all have in us an unknown reserve of energy that bursts forth when life puts us to the test. (Isabel Allende)",
            "Stars cannot shine without darkness. (Anonymous)"
    };

    private String message;

    public FortuneCookie(String message) {
        this.message = message;
    }

    @Override
    public void enjoy() {
        System.out.println("You got a fortune cookie!");
        System.out.println("\"" + message + "\"");
    }

    @Override
    public String toString() {
        return "[FortuneCookie] message = " + message;
    }

    public static FortuneCookie generate() {
        Random random = new Random();
        int quotesIndex = random.nextInt(quotes.length);
        return new FortuneCookie(quotes[quotesIndex]);
    }
}

final class GatherSurprises {

    private static final String[] surpriseTypes = {"FortuneCookie", "Candies", "MinionToy"};

    private GatherSurprises() {
    }

    public static ISurprise[] gather(int n) {
        ISurprise[] surprises = new ISurprise[n];
        for (int i = 0; i < n; i++) {
            surprises[i] = generateSurprise();
        }

        return surprises;
    }

    public static ISurprise gather() {
        return generateSurprise();
    }

    private static ISurprise generateSurprise() {
        Random random = new Random();
        int randomIndex = random.nextInt(surpriseTypes.length);
        String type = surpriseTypes[randomIndex];

        switch (type) {
            case "FortuneCookie":
                return FortuneCookie.generate();
            case "Candies":
                return Candies.generate();
            case "MinionToy":
                return MinionToy.generate();
            default:
                return null;
        }
    }
}

class GiveSurpriseAndApplause extends AbstractGiveSurprises {

    public GiveSurpriseAndApplause(String type, int waitTime) {
        super(type, waitTime);
    }

    @Override
    void giveWithPassion() {
        System.out.println("Loud applause to you... For it is in giving that we receive.");
    }
}

class GiveSurpriseAndHug extends AbstractGiveSurprises {

    public GiveSurpriseAndHug(String type, int waitTime) {
        super(type, waitTime);
    }

    @Override
    void giveWithPassion() {
        System.out.println("Warm wishes and a big hug!");
    }
}

class GiveSurpriseAndSing extends AbstractGiveSurprises {

    public GiveSurpriseAndSing(String type, int waitTime) {
        super(type, waitTime);
    }

    @Override
    void giveWithPassion() {
        System.out.println("Singing a nice song, full of joy and genuine excitement...");
    }
}

interface IBagFactory {

    // Create a new container, according to the specified type
    IBag makeBag(String type);
}

interface IBag {
    // adds a surprise in the bag
    void put(ISurprise newSurprise);

    // adds all the surprises from another IBag
    //   -> the 'bagOfSurprises' will be empty() afterwards
    void put(IBag bagOfSurprises);

    // removes a surprise from the bag and returns it
    ISurprise takeOut();

    // Checks if the bag is empty or not
    boolean isEmpty();

    // Returns the number of surprises
    int size();
}

interface ISurprise {
    // Opens the surprise and enjoys it
    void enjoy();
}

class MinionToy implements ISurprise {

    private static final String[] names = {"Dave", "Carl", "Kevin", "Stuart", "Jerry", "Tim"};
    private static int lastIndex = -1;
    private String name;

    public MinionToy(String name) {
        this.name = name;
    }

    @Override
    public void enjoy() {
        System.out.println("You got a minion named " + this.name + "!");
    }

    @Override
    public String toString() {
        return "[Minion] name = " + this.name;
    }

    public static MinionToy generate() {
        lastIndex = (lastIndex + 1) % names.length;
        return new MinionToy(names[lastIndex]);
    }
}

public class Main {
    private static final int CONSTRUCT = 0;
    private static final int PUT_BAG = 1;
    private static final int PUT_SURPRIZE = 2;
    private static final int TAKEOUT = 3;
    private static final int SIZE = 4;
    private static final int GIVE = 5;
    private static final int GIVE_ALL = 6;

    private static final int FORTUNE_COOKIE = 0;
    private static final int CANDIES = 1;
    private static final int MINION_TOY = 2;
    private static final int BAG_FIFO = 3;
    private static final int BAG_LIFO = 4;
    private static final int BAG_RANDOM = 5;
    private static final int GIVE_SURPRISE_AND_APPLAUSE = 6;
    private static final int GIVE_SURPRISE_AND_HUG = 7;
    private static final int GIVE_SURPRISE_AND_SING = 8;

    private static Map<String, Integer> commandsMap = createCommandsMap();

    private static Map<String, Integer> createCommandsMap() {
        Map<String, Integer> myMap = new HashMap<String, Integer>();
        myMap.put("construct", CONSTRUCT);
        myMap.put("putBag", PUT_BAG);
        myMap.put("putSurprize", PUT_SURPRIZE);
        myMap.put("takeout", TAKEOUT);
        myMap.put("size", SIZE);
        myMap.put("give", GIVE);
        myMap.put("giveAll", GIVE_ALL);

        return myMap;
    }

    private static Map<String, Integer> typesMap = createTypesMap();

    private static Map<String, Integer> createTypesMap() {
        Map<String, Integer> myMap = new HashMap<String, Integer>();
        myMap.put("FortuneCookie", FORTUNE_COOKIE);
        myMap.put("Candies", CANDIES);
        myMap.put("MinionToy", MINION_TOY);
        myMap.put("BagFIFO", BAG_FIFO);
        myMap.put("BagLIFO", BAG_LIFO);
        myMap.put("BagRandom", BAG_RANDOM);
        myMap.put("GiveSurpriseAndApplause", GIVE_SURPRISE_AND_APPLAUSE);
        myMap.put("GiveSurpriseAndHug", GIVE_SURPRISE_AND_HUG);
        myMap.put("GiveSurpriseAndSing", GIVE_SURPRISE_AND_SING);
        return myMap;
    }

    private static final String[] quotes = {
            "When you are content to simply be yourself and not compare yourself to others, everyone will respect you. (Lao Tse)",
            "Once you choose hope, anything is possible. (Christopher Reeve)",
            "I learned silence from the talkative, tolerance from the intolerant and kindness from the wicked. (Khalil Gibran)",
            "He who has no merit always envies the merits of others. (Francis Bacon)",
            "The highest measure of value is actually given during the confrontations to which life subjects you. (Anonymous)",
            "I discovered that if you love life, life will love you. (Arthur Rubinstein)",
            "Every life experience, every lesson I learn is the key to my future. (Clark Gable)",
            "The first condition to be happy is not to have time to think about unhappiness. (George Bernard Shaw)",
            "Give the best you have in you, because everything in your life belongs to you alone. (Ralph Waldo Emerson)",
            "He who lives in his own world is crazy. (Paulo Coelho)",
            "For every evil there are two cures: time and silence. (Alexandre Dumas)",
            "In life things are easy, only the wrong idea that it would be difficult stops us from being extraordinary. (Marian Rujoiu)",
            "Action may not bring you happiness, but there is no happiness without action. (William James)",
            "I never see what has been done. I only see what remains to be done. (Buddha)",
            "Don't waste your time knocking on a wall hoping you'll turn it into a door. (Coco Chanel)",
            "Attach yourself to those who can make you better and welcome those who, in turn, you can make better. (Seneca)",
            "Better to consume than to rust. (Denis Diderot)",
            "Wise is the one who lives every day as if every day and every hour he could die. (Francisco Gomez de Quevedo)",
            "We all have in us an unknown reserve of energy that bursts forth when life puts us to the test. (Isabel Allende)",
            "Stars cannot shine without darkness. (Anonymous)"
    };

    private static final String[] candyTypes = {"chocolate", "jelly", "fruits", "vanilla"};

    private static final String[] minionNames = {"Dave", "Carl", "Kevin", "Stuart", "Jerry", "Tim"};

    static FortuneCookie createFortuneCookie(String[] params, int index) {
        int qIndex = Integer.parseInt(params[index]);
        FortuneCookie fc = new FortuneCookie(quotes[qIndex % quotes.length]);
        return fc;
    }

    static Candies createCandies(String[] params, int index) {
        int num = Integer.parseInt(params[index++]);
        int tIndex = Integer.parseInt(params[index]);
        Candies candy = new Candies(num, candyTypes[tIndex % candyTypes.length]);
        return candy;
    }

    static MinionToy createMinionToy(String[] params, int index) {
        int mIndex = Integer.parseInt(params[index]);
        MinionToy minionToy = new MinionToy(minionNames[mIndex % minionNames.length]);
        return minionToy;
    }

    public static void main(String[] args) {
        File file = new File("resources/input.txt");

        Scanner scanner;
        if (file.exists()) {
            try {
                scanner = new Scanner(file);
            } catch (FileNotFoundException e) {
                System.out.println("Input file could not be found.");
                return;
            }
        } else {
            System.out.println("Input file could not be found - using command line.");
            scanner = new Scanner(System.in);
        }
        // dummy inits
        BagFIFO bagFifo = null;
        BagLIFO bagLifo = null;
        BagRandom bagRandom = null;
        ISurprise surprise = null;
        GiveSurpriseAndApplause giveSurpriseAndApplause = null;
        GiveSurpriseAndHug giveSurpriseAndHug = null;
        GiveSurpriseAndSing giveSurpriseAndSing = null;
        String bagType;
        IBag bag = null;

        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] params = line.split("\\s+");
            int i = 0;
            int waitTime = 0;
            String type;
            switch (commandsMap.get(params[i++])) {
                case CONSTRUCT:
                    switch (typesMap.get(params[i])) {
                        case FORTUNE_COOKIE:
                            FortuneCookie fc = createFortuneCookie(params, ++i);
                            System.out.println(fc.toString());
                            fc.enjoy();
                            break;
                        case CANDIES:
                            Candies c = createCandies(params, ++i);
                            System.out.println(c.toString());
                            c.enjoy();
                            break;
                        case MINION_TOY:
                            MinionToy minion = createMinionToy(params, ++i);
                            System.out.println(minion.toString());
                            minion.enjoy();
                            break;
                        case BAG_FIFO:
                            bagFifo = new BagFIFO();
                            break;
                        case BAG_LIFO:
                            bagLifo = new BagLIFO();
                            break;
                        case BAG_RANDOM:
                            bagRandom = new BagRandom();
                            break;
                        case GIVE_SURPRISE_AND_APPLAUSE:
                            type = params[++i];
                            waitTime = Integer.parseInt(params[++i]);
                            giveSurpriseAndApplause = new GiveSurpriseAndApplause(type, waitTime);
                            break;
                        case GIVE_SURPRISE_AND_HUG:
                            type = params[++i];
                            waitTime = Integer.parseInt(params[++i]);
                            giveSurpriseAndHug = new GiveSurpriseAndHug(type, waitTime);
                            break;
                        case GIVE_SURPRISE_AND_SING:
                            type = params[++i];
                            waitTime = Integer.parseInt(params[++i]);
                            giveSurpriseAndSing = new GiveSurpriseAndSing(type, waitTime);
                            break;
                    }
                    break;

                case PUT_BAG:
                    bagType = params[i];
                    i++;
                    switch (typesMap.get(params[i])) {
                        case BAG_FIFO:
                            bag = bagFifo;
                            break;
                        case BAG_LIFO:
                            bag = bagLifo;
                            break;
                        case BAG_RANDOM:
                            bag = bagRandom;
                            break;
                    }

                    switch (typesMap.get(bagType)) {
                        case BAG_FIFO:
                            bagFifo.put(bag);
                            break;
                        case BAG_LIFO:
                            bagLifo.put(bag);
                            break;
                        case BAG_RANDOM:
                            bagRandom.put(bag);
                            break;
                        case GIVE_SURPRISE_AND_APPLAUSE:
                            giveSurpriseAndApplause.put(bag);
                            break;
                        case GIVE_SURPRISE_AND_HUG:
                            giveSurpriseAndHug.put(bag);
                            break;
                        case GIVE_SURPRISE_AND_SING:
                            giveSurpriseAndSing.put(bag);
                            break;
                    }

                    break;

                case PUT_SURPRIZE:
                    bagType = params[i];
                    i++;
                    switch (typesMap.get(params[i])) {
                        case FORTUNE_COOKIE:
                            surprise = createFortuneCookie(params, ++i);
                            break;
                        case CANDIES:
                            surprise = createCandies(params, ++i);
                            break;
                        case MINION_TOY:
                            surprise = createMinionToy(params, ++i);
                            break;
                    }

                    switch (typesMap.get(bagType)) {
                        case BAG_FIFO:
                            bagFifo.put(surprise);
                            break;
                        case BAG_LIFO:
                            bagLifo.put(surprise);
                            break;
                        case BAG_RANDOM:
                            bagRandom.put(surprise);
                            break;
                        case GIVE_SURPRISE_AND_APPLAUSE:
                            giveSurpriseAndApplause.put(surprise);
                            break;
                        case GIVE_SURPRISE_AND_HUG:
                            giveSurpriseAndHug.put(surprise);
                            break;
                        case GIVE_SURPRISE_AND_SING:
                            giveSurpriseAndSing.put(surprise);
                            break;
                    }
                    break;

                case TAKEOUT:
                    switch (typesMap.get(params[i])) {
                        case BAG_FIFO:
                            surprise = bagFifo.takeOut();
                            surprise.enjoy();
                            break;
                        case BAG_LIFO:
                            surprise = bagLifo.takeOut();
                            surprise.enjoy();
                            break;
                        case BAG_RANDOM:
                            surprise = bagRandom.takeOut();
                            surprise.enjoy();
                            break;
                    }
                    break;

                case SIZE:
                    switch (typesMap.get(params[i])) {
                        case BAG_FIFO:
                            System.out.println(bagFifo.size());
                            break;
                        case BAG_LIFO:
                            System.out.println(bagLifo.size());
                            break;
                        case BAG_RANDOM:
                            System.out.println(bagRandom.size());
                            break;
                    }
                    break;

                case GIVE:
                    switch (typesMap.get(params[i])) {
                        case GIVE_SURPRISE_AND_APPLAUSE:
                            giveSurpriseAndApplause.give();
                            break;
                        case GIVE_SURPRISE_AND_HUG:
                            giveSurpriseAndHug.give();
                            break;
                        case GIVE_SURPRISE_AND_SING:
                            giveSurpriseAndSing.give();
                            break;
                    }
                    break;

                case GIVE_ALL:
                    switch (typesMap.get(params[i])) {
                        case GIVE_SURPRISE_AND_APPLAUSE:
                            giveSurpriseAndApplause.giveAll();
                            break;
                        case GIVE_SURPRISE_AND_HUG:
                            giveSurpriseAndHug.giveAll();
                            break;
                        case GIVE_SURPRISE_AND_SING:
                            giveSurpriseAndSing.giveAll();
                            break;
                    }
                    break;
            }
        }
    }
}

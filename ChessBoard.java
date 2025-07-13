import java.util.ArrayList;
import java.util.Arrays;

public class ChessBoard {
    private long white_pawn;
    private long black_pawn;
    private long white_knight;
    private long black_knight;
    private long white_bishop;
    private long black_bishop;
    private long white_rook;
    private long black_rook;
    private long white_queen;
    private long black_queen;
    private long white_king;
    private long black_king;

    private byte states;

    private static long[][] bhm = new long[64][];
    private static long[][] rhm = new long[64][];

    private static long[] rookMasks = new long[64];
    private static long[] bishopMasks = new long[64];
    private static int[] rookMaskSize= new int[64];
    private static int[] bishopMaskSize = new int[64];

    private static long notfileA = 0x7f7f7f7f7f7f7f7fL;
    private static long notfileAB = 0x3f3f3f3f3f3f3f3fL;
    private static long notfileH = 0xfefefefefefefefeL;
    private static long notfileGH = 0xfcfcfcfcfcfcfcfcL;

    private static long[] magicRooks = new long[]{
            212617675778L, 282094022498308L, -9068560455915011030L, 2344686630157158402L, 594484086560522497L, 4612250351495618690L, 126100862736277633L, 2351180272764199042L,
            1153221125842141696L, 4785093964988672L, 6794984007270528L, 52798062854400L, 159877855516330112L, 1152956697837338752L, 585468644121739392L, 140776279785728L,
            281622087794690L, 81627747613802504L, 290625112544149632L, 1360249815321051264L, 45071455691603984L, 2305878744717459520L, 36170635092885540L, 3171132546930999297L,
            18159537567567247L, 18155226200545792L, 9055579922244096L, 36173934709721088L, 1164181053573713920L, -3422700530273611776L, 18014570335440896L, 36030033973739584L,
            2306160501786936449L, 17609366446338L, 9570157799146632L, 8798240768128L, 2305860603555612672L, 56368666918256912L, 72344021114175488L, 36064606309072896L,
            -4480797954553936703L, 76565593138397712L, 72621093780979840L, 3531104682619742208L, 442338475790831616L, 2399504907762668800L, 49612438548516864L, 150083349774368L,
            1153484456712737297L, 281547999674436L, 5224316331008196736L, 1153062276480115712L, 3819334100721860680L, 2305983815429922821L, 70506187329538L, 119486142683807776L,
            648527284168310914L, 3530826583220716800L, 612491198623384576L, 72059999295635712L, 8106496921528436737L, 72066459387314432L, 18031991769276416L, 36029071907391122L,
    };

    private static long[] magicBishops = new long[]{
            2314956878293290496L, 180153399734960196L, 144124259093214468L, 4650599934228566529L, 45213434362593800L, 27313020439365768L, 3170536337967286274L, 4901605797550764032L,
            621505897201445444L, -1995059072578568190L, -3312041214067998720L, 962173634560L, 70371999088640L, 585538600885551104L, 41377925655831041L, 577588993240596484L,
            581169246642688L, 4944959073838694464L, 73184611039453440L, 149555123322944L, 18023332444111104L, 288301022466543616L, 1153064445950398481L, -8645784180977817344L,
            5775874219774640193L, 2261276659091456L, -3456227914724866048L, 18015502334955584L, 178127326675584L, 1126484047709696L, -9223072943384782812L, 190422357855228416L,
            46163133148463620L, 429359358020624L, -9221681949473337344L, -4323174095307915248L, 4761993793564082720L, 2282586273562640L, 2310351015746207936L, 4686004217517708544L,
            4617249551597047816L, 1134975182311440L, 1155314059357459200L, 8358967366075809792L, -4575226109737549692L, 4503758559117456L, 1522216691252199944L, 162134604772811008L,
            4397766137920718848L, -4539020360065867262L, 576462987901272064L, 171176385976483848L, -4521609597735141008L, 5044040415994085384L, 1153488859720384768L, 1143509944402944L,
            9288957967796240L, 72602664583296L, 565217897480225L, 4612815251247747081L, 19144702905876498L, -8993610748877856510L, 2534391624630528L, 3149018498613760L,
    };

    public static void makeEverything() {
        for (int i = 0; i < 64; i++) {
            int x = i % 8;
            int y = i / 8;
            long rowMask = (0xff00000000000000L >>> (y * 8)) & ~0x8080808080808080L & ~(0x8080808080808080L >>> 7);
            long colMask = (0x8080808080808080L >>> x) & ~0xff00000000000000L & ~(0xff00000000000000L >>> 56);
            rookMasks[i] = (rowMask | colMask) & ~(0x8000000000000000L >>> i);
            rookMaskSize[i] = Long.bitCount(rookMasks[i]);
        }

        for (int i = 0; i < 64; i++) {
            int x = i % 8;
            int y = i / 8;
            int a = x + y - 7;
            int b = x - y;
            long xyMask = (b > 0) ? 0x8040201008040201L << (b * 8) : 0x8040201008040201L >>> (-b * 8);
            long x_yMask = (a > 0) ? 0x102040810204080L >>> (a * 8) : 0x102040810204080L << (-a * 8);
            bishopMasks[i] = (xyMask | x_yMask) & ~(0x8000000000000000L >>> i) & ~0xff818181818181ffL;
            bishopMaskSize[i] = Long.bitCount(bishopMasks[i]);
        }

        for (int i = 0; i < 64; i++) {
            bhm[i] = new long[(int) Math.pow(2, amount(i, true))];
            rhm[i] = new long[(int) Math.pow(2, amount(i, false))];
            ArrayList<Long> poss = findPossibilities(i, true);
            ArrayList<Long> poss2 = findPossibilities(i, false);

            for (int j = 0; j < poss.size(); j++) {
                long ps = poss.get(j);
                bhm[i][(int) (magicBishops[i] * ps >>> (64 - bishopMaskSize[i]))] = slowMoves(i, ps, true);
            }
            for (int j = 0; j < poss2.size(); j++) {
                long ps = poss2.get(j);
                rhm[i][(int) (magicRooks[i] * ps >>> (64 - rookMaskSize[i]))] = slowMoves(i, ps, false);
            }
        }
    }

    public ChessBoard() {
        white_pawn = 0xff00L;
        black_pawn = 0xff000000000000L;
        white_knight = 0x42L;
        black_knight = 0x4200000000000000L;
        white_bishop = 0x24L;
        black_bishop = 0x2400000000000000L;
        white_rook = 0x81L;
        black_rook = 0x8100000000000000L;
        white_queen = 0x10L;
        black_queen = 0x1000000000000000L;
        white_king = 0x8L;
        black_king = 0x800000000000000L;

        states = (byte) 0x3; // 00 RookA, RookH, NotKing, White Black
    }

    public ChessBoard(ChessBoard b) {
        long[] stuff = b.getBoard();
        byte stuffs = b.getPieceStates();
        white_pawn = stuff[0];
        black_pawn = stuff[1];
        white_knight = stuff[2];
        black_knight = stuff[3];
        white_bishop = stuff[4];
        black_bishop = stuff[5];
        white_rook = stuff[6];
        black_rook = stuff[7];
        white_queen = stuff[8];
        black_queen = stuff[9];
        white_king = stuff[10];
        black_king = stuff[11];

        states = stuffs;
    }

    public static ArrayList<Long> findPossibilities(int l, boolean bishop) {
        ArrayList<Integer> consideredBits = new ArrayList<>();
        ArrayList<Long> sol = new ArrayList<>();
        int x = l % 8;
        int y = l / 8;
        if (bishop) {
            do {
                x++;
                y++;

                if (x > 0 && x < 7 && y > 0 && y < 7) {
                    consideredBits.add(y * 8 + x);
                }
            }
            while (x > 0 && x < 7 && y > 0 && y < 7);

            x = l % 8;
            y = l / 8;

            do {
                x--;
                y++;

                if (x > 0 && x < 7 && y > 0 && y < 7) {
                    consideredBits.add(y * 8 + x);
                }
            }
            while (x > 0 && x < 7 && y > 0 && y < 7);

            x = l % 8;
            y = l / 8;

            do {
                x++;
                y--;

                if (x > 0 && x < 7 && y > 0 && y < 7) {
                    consideredBits.add(y * 8 + x);
                }
            }
            while (x > 0 && x < 7 && y > 0 && y < 7);

            x = l % 8;
            y = l / 8;

            do {
                x--;
                y--;

                if (x > 0 && x < 7 && y > 0 && y < 7) {
                    consideredBits.add(y * 8 + x);
                }
            }
            while (x > 0 && x < 7 && y > 0 && y < 7);
        }
        else {
            for (int i = 1; i < 7; i++) {
                if (i == y) {
                    continue;
                }
                consideredBits.add(x + i * 8);
            }

            for (int i = 1; i < 7; i++) {
                if (i == x) {
                    continue;
                }
                consideredBits.add(i + y * 8);
            }
        }

        for (int i = 0; i < Math.pow(2, consideredBits.size()); i++) {
            long a = 0;
            for (int j = 0; j < consideredBits.size(); j++) {
                boolean b = (i & (1 << (j))) != 0;
                a |= ((b ? 1L : 0L) << (63 - consideredBits.get(j)));
            }
            sol.add(a);
        }

        return sol;
    }

    public static int amount(int l, boolean bishop) {
        int s = 0;

        int x = l % 8;
        int y = l / 8;
        if (bishop) {

            do {
                x++;
                y++;

                if (x > 0 && x < 7 && y > 0 && y < 7) {
                    s++;
                }
            }
            while (x > 0 && x < 7 && y > 0 && y < 7);

            x = l % 8;
            y = l / 8;

            do {
                x--;
                y++;

                if (x > 0 && x < 7 && y > 0 && y < 7) {
                    s++;
                }
            }
            while (x > 0 && x < 7 && y > 0 && y < 7);

            x = l % 8;
            y = l / 8;

            do {
                x++;
                y--;

                if (x > 0 && x < 7 && y > 0 && y < 7) {
                    s++;
                }
            }
            while (x > 0 && x < 7 && y > 0 && y < 7);

            x = l % 8;
            y = l / 8;

            do {
                x--;
                y--;

                if (x > 0 && x < 7 && y > 0 && y < 7) {
                    s++;
                }
            }
            while (x > 0 && x < 7 && y > 0 && y < 7);
        }
        else {
            s = 10;
            if (x == 0 || x == 7) {
                s += 1;
            }
            if (y == 0 || y == 7) {
                s += 1;
            }
        }
        return s;
    }

    public static long slowMoves(int l, long blockers, boolean bishop) {
        // use bitboards??

        long start = 0x8000000000000000L >>> l;

        long sol;
        long solSol = 0x0L;

        long changeOpp;

        if (bishop) {
            changeOpp = notfileH & (~((notfileA & blockers) << 9));
            sol = (start << 9) & changeOpp;
            sol |= (sol << 9) & changeOpp;
            changeOpp &= (changeOpp << 9);
            sol |= (sol << 18) & changeOpp;
            changeOpp &= (changeOpp << 18);
            sol |= (sol << 36) & changeOpp;
            solSol |= (sol) & notfileH;

            changeOpp = notfileH & (~((notfileA & blockers) >>> 7));
            sol = (start >>> 7) & changeOpp;
            sol |= (sol >>> 7) & changeOpp;
            changeOpp &= (changeOpp >>> 7);
            sol |= (sol >>> 14) & changeOpp;
            changeOpp &= (changeOpp >>> 14);
            sol |= (sol >>> 28) & changeOpp;
            solSol |= (sol) & notfileH;

            changeOpp = notfileA & (~((notfileH & blockers) << 7));
            sol = (start << 7) & changeOpp;
            sol |= (sol << 7) & changeOpp;
            changeOpp &= (changeOpp << 7);
            sol |= (sol << 14) & changeOpp;
            changeOpp &= (changeOpp << 14);
            sol |= (sol << 28) & changeOpp;
            solSol |= (sol) & notfileA;

            changeOpp = notfileA & (~((notfileH & blockers) >>> 9));
            sol = (start >>> 9) & changeOpp;
            sol |= (sol >>> 9) & changeOpp;
            changeOpp &= (changeOpp >>> 9);
            sol |= (sol >>> 18) & changeOpp;
            changeOpp &= (changeOpp >>> 18);
            sol |= (sol >>> 36) & changeOpp;
            solSol |= (sol) & notfileA;
        }
        else {
            changeOpp = notfileH & (~((notfileA & blockers) << 1));
            sol = (start << 1) & changeOpp;
            sol |= (sol << 1) & changeOpp;
            changeOpp &= (changeOpp << 1);
            sol |= (sol << 2) & changeOpp;
            changeOpp &= (changeOpp << 2);
            sol |= (sol << 4) & changeOpp;
            solSol |= (sol) & notfileH;

            changeOpp = ~(blockers << 8);
            sol = (start << 8) & changeOpp;
            sol |= (sol << 8) & changeOpp;
            changeOpp &= (changeOpp << 8);
            sol |= (sol << 16) & changeOpp;
            changeOpp &= (changeOpp << 16);
            sol |= (sol << 32) & changeOpp;
            solSol |= (sol);

            changeOpp = notfileA & (~((notfileH & blockers) >>> 1));
            sol = (start >>> 1) & changeOpp;
            sol |= (sol >>> 1) & changeOpp;
            changeOpp &= (changeOpp >>> 1);
            sol |= (sol >>> 2) & changeOpp;
            changeOpp &= (changeOpp >>> 2);
            sol |= (sol >>> 4) & changeOpp;
            solSol |= (sol) & notfileA;

            changeOpp = (~(blockers >>> 8) & ~start);
            sol = (start >>> 8) & changeOpp;
            sol |= (sol >>> 8) & changeOpp;
            changeOpp &= (changeOpp >>> 8);
            sol |= (sol >>> 16) & changeOpp;
            changeOpp &= (changeOpp >>> 16);
            sol |= (sol >>> 32) & changeOpp;
            solSol |= (sol);
        }
        return solSol;
    }

    public void move(int m) {
        int from = m >>> 26;
        int to = (m << 6) >>> 26;
        int piece = (m << 12) >>> 29;
        int side = (m << 15) >>> 31;
        int promotion = (m << 16) >>> 29;
        int ep = (m << 19) >>> 31;

        long startingPiece = 0x8000000000000000L >>> from;
        long endingPiece = 0x8000000000000000L >>> to;

        if (piece == 5) {
            if (side == 1) {
                states &= (byte) 0xfe;
                if (from - to == 2) {
                    white_king = 0x20L;
                    white_rook ^= startingPiece << 4;
                    white_rook |= endingPiece >>> 1;
                    return;
                }
                else if (from - to == -2) {
                    white_king = 0x2L;
                    white_rook ^= startingPiece >>> 3;
                    white_rook |= endingPiece << 1;
                    return;
                }
            }
            else {
                states &= (byte) 0xfd;
                if (from - to == 2) {
                    black_king = 0x2000000000000000L;
                    black_rook ^= startingPiece << 4;
                    black_rook |= endingPiece >>> 1;
                    return;
                }
                else if (from - to == -2) {
                    black_king = 0x200000000000000L;
                    black_rook ^= startingPiece >>> 3;
                    black_rook |= endingPiece << 1;
                    return;
                }
            }
        }
        else if (piece == 3) {
            if (from % 8 == 0) {
                states |= (byte) (0x20 >>> side);
            }
            else if (from % 8 == 7) {
                states |= (byte) (0x8 >>> side);
            }
        }

        if (promotion != 0) {
            long notthisPiece = ~endingPiece;
            if (side == 1) {

                white_pawn ^= startingPiece;
                if (promotion == 1) {
                    white_knight |= endingPiece;
                }
                else if (promotion == 2) {
                    white_bishop |= endingPiece;
                }
                else if (promotion == 3) {
                    white_rook |= endingPiece;
                }
                else if (promotion == 4) {
                    white_queen |= endingPiece;
                }

                black_pawn &= notthisPiece;
                black_knight &= notthisPiece;
                black_bishop &= notthisPiece;
                black_rook &= notthisPiece;
                black_queen &= notthisPiece;

            }
            else {

                black_pawn ^= startingPiece;
                if (promotion == 1) {
                    black_knight |= endingPiece;
                }
                else if (promotion == 2) {
                    black_bishop |= endingPiece;
                }
                else if (promotion == 3) {
                    black_rook |= endingPiece;
                }
                else if (promotion == 4) {
                    black_queen |= endingPiece;
                }

                white_pawn &= notthisPiece;
                white_knight &= notthisPiece;
                white_bishop &= notthisPiece;
                white_rook &= notthisPiece;
                white_queen &= notthisPiece;
            }
        }
        else {
            long notthisPiece = ~endingPiece;
            if (side == 1) {

                // white
                if (piece == 0) {
                    white_pawn ^= startingPiece;
                    white_pawn |= endingPiece;
                }
                else if (piece == 1){
                    white_knight ^= startingPiece;
                    white_knight |= endingPiece;
                }
                else if (piece == 2){
                    white_bishop ^= startingPiece;
                    white_bishop |= endingPiece;
                }
                else if (piece == 3){
                    white_rook ^= startingPiece;
                    white_rook |= endingPiece;
                }
                else if (piece == 4){
                    white_queen ^= startingPiece;
                    white_queen |= endingPiece;
                }
                else if (piece == 5){
                    white_king ^= startingPiece;
                    white_king |= endingPiece;
                }

                black_pawn &= notthisPiece;
                black_knight &= notthisPiece;
                black_bishop &= notthisPiece;
                black_rook &= notthisPiece;
                black_queen &= notthisPiece;

                if (ep != 0) {
                    black_pawn &= ~(endingPiece >>> 8);
                }
            }
            else {

                // black
                if (piece == 0) {
                    black_pawn ^= startingPiece;
                    black_pawn |= endingPiece;
                }
                else if (piece == 1){
                    black_knight ^= startingPiece;
                    black_knight |= endingPiece;
                }
                else if (piece == 2){
                    black_bishop ^= startingPiece;
                    black_bishop |= endingPiece;
                }
                else if (piece == 3){
                    black_rook ^= startingPiece;
                    black_rook |= endingPiece;
                }
                else if (piece == 4){
                    black_queen ^= startingPiece;
                    black_queen |= endingPiece;
                }
                else if (piece == 5){
                    black_king ^= startingPiece;
                    black_king |= endingPiece;
                }

                white_pawn &= notthisPiece;
                white_knight &= notthisPiece;
                white_bishop &= notthisPiece;
                white_rook &= notthisPiece;
                white_queen &= notthisPiece;

                if (ep != 0) {
                    white_pawn &= ~(endingPiece << 8);
                }
            }
        }
    }

    public void unmove(int m, byte before) {
        int from = m >>> 26;
        int to = (m << 6) >>> 26;
        int piece = (m << 12) >>> 29;
        int side = (m << 15) >>> 31;
        int promotion = (m << 16) >>> 29;
        int ep = (m << 19) >>> 31;
        int eaten_piece = (m << 20) >>> 29;

        long fPiece = (0x8000000000000000L >>> from);
        long tPiece = (0x8000000000000000L >>> to);

        states = before;

        // Check for castle

        if (piece == 5 && from - to == 2) {
            // Queen-side castle
            if (side == 1) {
                white_king |= fPiece;
                white_king ^= tPiece;
                white_rook |= tPiece << 2;
                white_rook ^= tPiece >>> 1;
            }
            else {
                black_king |= fPiece;
                black_king ^= tPiece;
                black_rook |= tPiece << 2;
                black_rook ^= tPiece >>> 1;
            }
            return;
        }
        else if (piece == 5 && from - to == -2) {
            // King-side castle
            if (side == 1) {
                white_king |= fPiece;
                white_king ^= tPiece;
                white_rook |= tPiece >>> 1;
                white_rook ^= tPiece << 1;
            }
            else {
                black_king |= fPiece;
                black_king ^= tPiece;
                black_rook |= tPiece >>> 1;
                black_rook ^= tPiece << 1;
            }
            return;
        }

        // Check for en passant

        if (ep != 0) {
            if (side == 1) {
                black_pawn |= tPiece >>> 8;
                white_pawn ^= tPiece;
                white_pawn |= fPiece;
            }
            else {
                white_pawn |= tPiece << 8;
                black_pawn ^= tPiece;
                black_pawn |= fPiece;
            }
            return;
        }

        // Other types of moves

        if (promotion > 0) {
            if (side == 1) {
                if (promotion == 1) {
                    white_knight ^= tPiece;
                } else if (promotion == 2) {
                    white_bishop ^= tPiece;
                } else if (promotion == 3) {
                    white_rook ^= tPiece;
                } else if (promotion == 4) {
                    white_queen ^= tPiece;
                }
                white_pawn |= fPiece;
            }
            else {
                if (promotion == 1) {
                    black_knight ^= tPiece;
                } else if (promotion == 2) {
                    black_bishop ^= tPiece;
                } else if (promotion == 3) {
                    black_rook ^= tPiece;
                } else if (promotion == 4) {
                    black_queen ^= tPiece;
                }
                black_pawn |= fPiece;
            }
        }
        else {
            if (piece == 0) {
                if (side == 1) {
                    white_pawn ^= tPiece;
                    white_pawn |= fPiece;
                }
                else {
                    black_pawn ^= tPiece;
                    black_pawn |= fPiece;
                }
            }
            else if (piece == 1) {
                if (side == 1) {
                    white_knight ^= tPiece;
                    white_knight |= fPiece;
                }
                else {
                    black_knight ^= tPiece;
                    black_knight |= fPiece;
                }
            }
            else if (piece == 2) {
                if (side == 1) {
                    white_bishop ^= tPiece;
                    white_bishop |= fPiece;
                }
                else {
                    black_bishop ^= tPiece;
                    black_bishop |= fPiece;
                }
            }
            else if (piece == 3) {
                if (side == 1) {
                    white_rook ^= tPiece;
                    white_rook |= fPiece;
                }
                else {
                    black_rook ^= tPiece;
                    black_rook |= fPiece;
                }
            }
            else if (piece == 4) {
                if (side == 1) {
                    white_queen ^= tPiece;
                    white_queen |= fPiece;
                }
                else {
                    black_queen ^= tPiece;
                    black_queen |= fPiece;
                }
            }
            else if (piece == 5) {
                if (side == 1) {
                    white_king ^= tPiece;
                    white_king |= fPiece;
                }
                else {
                    black_king ^= tPiece;
                    black_king |= fPiece;
                }
            }
        }

        // Return captured piece

        if (eaten_piece != 5) {
            if (eaten_piece == 4) {
                if (side == 1) {
                    black_queen |= tPiece;
                }
                else {
                    white_queen |= tPiece;
                }
            }
            else if (eaten_piece == 3) {
                if (side == 1) {
                    black_rook |= tPiece;
                }
                else {
                    white_rook |= tPiece;
                }
            }
            else if (eaten_piece == 2) {
                if (side == 1) {
                    black_bishop |= tPiece;
                }
                else {
                    white_bishop |= tPiece;
                }
            }
            else if (eaten_piece == 1) {
                if (side == 1) {
                    black_knight |= tPiece;
                }
                else {
                    white_knight |= tPiece;
                }
            }
            else if (eaten_piece == 0) {
                if (side == 1) {
                    black_pawn |= tPiece;
                }
                else {
                    white_pawn |= tPiece;
                }
            }
        }
    }

    public int gameState(int lastMove) {
        // -1 = checkmate, 0 = stalemate, 1 = continue;
        int from = lastMove >>> 26;
        int to = (lastMove << 6) >>> 26;
        int piece = (lastMove << 12) >>> 29;
        int side = (lastMove << 15) >>> 31;

        int croist = -1;
        if (piece == 0) {
            if (to - from == 16 || from - to == 16) {
                croist = to % 8;
            }
        }

        int[] moves = nGetMoves(!(side == 1), croist);

        if (moves[0] == 1) {
            return 0;
        }
        if (moves[0] == 2) {
            return -1;
        }
        if ((white_pawn | white_knight | white_bishop | white_rook | white_queen |
        black_pawn | black_knight | black_bishop | black_rook | black_queen) == 0){
            return 0;
        }
        return 1;
    }

    public long[] getBoard() {
        return new long[]{white_pawn, black_pawn,
                white_knight, black_knight,
                white_bishop, black_bishop,
                white_rook, black_rook,
                white_queen, black_queen,
                white_king, black_king
        };
    }

    public byte getPieceStates() {
        return states;
    }

    public boolean equals(ChessBoard b) {
        long[] board = b.getBoard();
        byte statate = b.getPieceStates();
        return (board[0] == white_pawn && board[1] == black_pawn && board[2] == white_knight && board[3] == black_knight
        && board[4] == white_bishop && board[5] == black_bishop && board[6] == white_rook && board[7] == black_rook
        && board[8] == white_queen && board[9] == black_queen && board[10] == white_king && board[11] == black_king) &&
                statate == states;
    }

    public int[] nGetMoves(boolean white, int croissant) {
        // move format: int, from, to, type, side, en passant, promotion, capture = 23 bits;
        // type: 0 = pawn, 1 = knight, 2 = bishop, 3 = rook, 4 = queen, 5 = king
        // capture: 0 = pawn, 1 = knight, 2 = bishop, 3 = rook, 4 = queen, 5 = empty

        long[] pin_rays = new long[]{
                -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L,
                -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L,
                -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L,
                -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L
        };

        int index = 1;
        int[] moves = new int[256];

        long pawn = white ? white_pawn : black_pawn;
        long knight = white ? white_knight : black_knight;
        long bishop = white ? white_bishop : black_bishop;
        long rook = white ? white_rook : black_rook;
        long queen = white ? white_queen : black_queen;
        long king = white ? white_king : black_king;
        long all_pieces = white_pawn | black_pawn |
                white_knight | black_knight |
                white_rook | black_rook |
                white_bishop | black_bishop |
                white_queen | black_queen |
                white_king | black_king;
        long opp_pieces = white ? black_pawn | black_knight | black_rook | black_bishop | black_queen | black_king :
                white_pawn | white_knight | white_rook | white_bishop | white_queen | white_king;
        long my_pieces = all_pieces ^ opp_pieces;
        long no_pieces = ~all_pieces;

        long opp_bishop = white ? black_bishop : white_bishop;
        long opp_rook = white ? black_rook : white_rook;
        long opp_queen = white ? black_queen : white_queen;
        long opp_king = white ? black_king : white_king;
        long opp_knight = white ? black_knight : white_knight;
        long opp_pawn = white ? black_pawn : white_pawn;

        int kingLocation = Long.numberOfLeadingZeros(king);
        long can_pin = opp_bishop | opp_queen;
        long cannot_pin = all_pieces ^ can_pin;
        long king_cannot_move = 0x0L;
        long checks = 0x0L;
        int[] locs;

        if (kingLocation % 8 != 0 && kingLocation % 8 != 7) {
            if (kingLocation / 8 == 0) {
                locs = new int[]{-1, 1, 7, 8, 9};
            }
            else if (kingLocation / 8 == 7) {
                locs = new int[]{-1, 1, -7, -8, -9};

            }
            else {
                locs = new int[]{-1, 1, 7, 8, 9, -7, -8, -9};

            }
        }
        else if (kingLocation % 8 == 0) {
            if (kingLocation / 8 == 0) {
                locs = new int[]{1, 8, 9};
            }
            else if (kingLocation / 8 == 7) {
                locs = new int[]{1, -8, -7};
            }
            else {
                locs = new int[]{1, -8, -7, 8, 9};
            }
        }
        else {
            if (kingLocation / 8 == 0) {
                locs = new int[]{-1, 8, 7};
            }
            else if (kingLocation / 8 == 7) {
                locs = new int[]{-1, -8, -9};
            }
            else {
                locs = new int[]{-1, 8, 7, -8, -9};
            }
        }

        long thePiece = 0x8000000000000000L >>> kingLocation;

        long blockerss = all_pieces & bishopMasks[kingLocation];
        long movee = bhm[kingLocation][(int) ((magicBishops[kingLocation] * blockerss) >>> (64 - bishopMaskSize[kingLocation]))];
        movee &= (opp_bishop | opp_queen);

        checks |= movee;

        blockerss = all_pieces & rookMasks[kingLocation];
        movee = rhm[kingLocation][(int) ((magicRooks[kingLocation] * blockerss) >>> (64 - rookMaskSize[kingLocation]))];
        movee &= (opp_rook | opp_queen);

        checks |= movee;

        long notPieceFileAA = thePiece & notfileA;
        long notPieceFileABB = thePiece & notfileAB;
        long notPieceFileHA = thePiece & notfileH;
        long notPieceFileGHH = thePiece & notfileGH;
        movee = white ? (notPieceFileAA << 9) : (notPieceFileHA >>> 9);
        movee |= white ? (notPieceFileHA << 7) : (notPieceFileAA >>> 7);
        movee &= opp_pawn;

        checks |= movee;

        movee = notPieceFileAA << 17;
        movee |= notPieceFileHA << 15;
        movee |= notPieceFileABB << 10;
        movee |= notPieceFileGHH << 6;
        movee |= notPieceFileABB >>> 6;
        movee |= notPieceFileGHH >>> 10;
        movee |= notPieceFileAA >>> 15;
        movee |= notPieceFileHA >>> 17;
        movee &= opp_knight;

        checks |= movee;

        for (int i : locs) {
            int loc = kingLocation + i;
            long myPiece = 0x8000000000000000L >>> loc;

            long blockers = all_pieces & ~king & bishopMasks[loc];
            long move = bhm[loc][(int) ((magicBishops[loc] * blockers) >>> (64 - bishopMaskSize[loc]))];
            move &= (opp_bishop | opp_queen);

            if (move != 0) {
                king_cannot_move |= myPiece;
                continue;
            }

            blockers = all_pieces & ~king & rookMasks[loc];
            move = rhm[loc][(int) ((magicRooks[loc] * blockers) >>> (64 - rookMaskSize[loc]))];
            move &= (opp_rook | opp_queen);

            if (move != 0) {
                king_cannot_move |= myPiece;
                continue;
            }

            long notPieceFileA = myPiece & notfileA;
            long notPieceFileAB = myPiece & notfileAB;
            long notPieceFileH = myPiece & notfileH;
            long notPieceFileGH = myPiece & notfileGH;


            move = white ? (notPieceFileA << 9) : (notPieceFileH >>> 9);
            move |= white ? (notPieceFileH << 7) : (notPieceFileA >>> 7);
            move &= opp_pawn;

            if (move != 0) {
                king_cannot_move |= myPiece;
                continue;
            }

            move = notPieceFileA << 17;
            move |= notPieceFileH << 15;
            move |= notPieceFileAB << 10;
            move |= notPieceFileGH << 6;
            move |= notPieceFileAB >>> 6;
            move |= notPieceFileGH >>> 10;
            move |= notPieceFileA >>> 15;
            move |= notPieceFileH >>> 17;
            move &= opp_knight;

            if (move != 0) {
                king_cannot_move |= myPiece;
                continue;
            }

            move = (notPieceFileA) << 9;
            move |= myPiece << 8;
            move |= notPieceFileH << 7;
            move |= notPieceFileA << 1;
            move |= notPieceFileH >>> 1;
            move |= notPieceFileA >>> 7;
            move |= myPiece >>> 8;
            move |= notPieceFileH >>> 9;
            move &= opp_king;

            if (move != 0) {
                king_cannot_move |= myPiece;
            }
        }

        int loca = kingLocation;
        int pinned_piece = -1;
        while (true) {

            if ((loca % 8) == 0 || loca < 9) {
                break;
            }
            loca -= 9;

            long king_piece = 0x8000000000000000L >>> loca;
            if ((king_piece & my_pieces) != 0) {
                if (pinned_piece >= 0) {
                    break;
                }
                else {
                    pinned_piece = loca;
                }
            }
            else if ((king_piece & cannot_pin) != 0) {
                break;
            }
            else if ((king_piece & can_pin) != 0) {
                if (pinned_piece >= 0) {
                    int shift = 8 * (pinned_piece / 8 - pinned_piece % 8);
                    pin_rays[pinned_piece] = (shift > 0) ? 0x8040201008040201L >>> shift : 0x8040201008040201L << -shift;
                }
                break;
            }
        }

        loca = kingLocation;
        pinned_piece = -1;
        while (true) {

            if ((loca % 8) == 7 || loca < 8) {
                break;
            }
            loca -= 7;

            long king_piece = 0x8000000000000000L >>> loca;
            if ((king_piece & my_pieces) != 0) {
                if (pinned_piece >= 0) {
                    break;
                }
                else {
                    pinned_piece = loca;
                }
            }
            else if ((king_piece & cannot_pin) != 0) {
                break;
            }
            else if ((king_piece & can_pin) != 0) {
                if (pinned_piece >= 0) {
                    int shift = 8 * (pinned_piece / 8 + pinned_piece % 8 - 7);
                    pin_rays[pinned_piece] = (shift > 0) ? 0x102040810204080L >>> shift : 0x102040810204080L << -shift;
                }
                break;
            }
        }

        loca = kingLocation;
        pinned_piece = -1;
        while (true) {

            if ((loca % 8) == 0 || loca > 56) {
                break;
            }
            loca += 7;

            long king_piece = 0x8000000000000000L >>> loca;
            if ((king_piece & my_pieces) != 0) {
                if (pinned_piece >= 0) {
                    break;
                }
                else {
                    pinned_piece = loca;
                }
            }
            else if ((king_piece & cannot_pin) != 0) {
                break;
            }
            else if ((king_piece & can_pin) != 0) {
                if (pinned_piece >= 0) {
                    int shift = 8 * (pinned_piece / 8 + pinned_piece % 8 - 7);
                    pin_rays[pinned_piece] = (shift > 0) ? 0x102040810204080L >>> shift : 0x102040810204080L << -shift;
                }
                break;
            }
            else if ((king_piece & opp_pieces) != 0) {
                break;
            }
        }

        loca = kingLocation;
        pinned_piece = -1;
        while (true) {

            if ((loca % 8) == 7 || loca > 55) {
                break;
            }
            loca += 9;

            long king_piece = 0x8000000000000000L >>> loca;
            if ((king_piece & my_pieces) != 0) {
                if (pinned_piece >= 0) {
                    break;
                }
                else {
                    pinned_piece = loca;
                }
            }
            else if ((king_piece & cannot_pin) != 0) {
                break;
            }
            else if ((king_piece & can_pin) != 0) {
                if (pinned_piece >= 0) {
                    int shift = 8 * (pinned_piece / 8 - pinned_piece % 8);
                    pin_rays[pinned_piece] = (shift > 0) ? 0x8040201008040201L >>> shift : 0x8040201008040201L << -shift;
                }
                break;
            }
        }

        can_pin = opp_queen | opp_rook;
        cannot_pin = all_pieces ^ can_pin;

        loca = kingLocation;
        pinned_piece = -1;
        while (true) {

            if (loca < 8) {
                break;
            }
            loca -= 8;

            long king_piece = 0x8000000000000000L >>> loca;
            if ((king_piece & my_pieces) != 0) {
                if (pinned_piece >= 0) {
                    break;
                }
                else {
                    pinned_piece = loca;
                }
            }
            else if ((king_piece & cannot_pin) != 0) {
                break;
            }
            else if ((king_piece & can_pin) != 0) {
                if (pinned_piece >= 0) {
                    pin_rays[pinned_piece] = 0x8080808080808080L >>> (pinned_piece % 8);
                }
                break;
            }
        }

        loca = kingLocation;
        pinned_piece = -1;
        while (true) {

            if (loca % 8 == 7) {
                break;
            }
            loca += 1;

            long king_piece = 0x8000000000000000L >>> loca;
            if ((king_piece & my_pieces) != 0) {
                if (pinned_piece >= 0) {
                    break;
                }
                else {
                    pinned_piece = loca;
                }
            }
            else if ((king_piece & cannot_pin) != 0) {
                break;
            }
            else if ((king_piece & can_pin) != 0) {
                if (pinned_piece >= 0) {
                    pin_rays[pinned_piece] = 0xff00000000000000L >>> 8 * (pinned_piece / 8);
                }
                break;
            }
        }

        loca = kingLocation;
        pinned_piece = -1;
        while (true) {

            if (loca > 55) {
                break;
            }
            loca += 8;

            long king_piece = 0x8000000000000000L >>> loca;
            if ((king_piece & my_pieces) != 0) {
                if (pinned_piece >= 0) {
                    break;
                }
                else {
                    pinned_piece = loca;
                }
            }
            else if ((king_piece & cannot_pin) != 0) {
                break;
            }
            else if ((king_piece & can_pin) != 0) {
                if (pinned_piece >= 0) {
                    pin_rays[pinned_piece] = 0x8080808080808080L >>> (pinned_piece % 8);
                }
                break;
            }
        }

        loca = kingLocation;
        pinned_piece = -1;
        while (true) {

            if (loca % 8 == 0) {
                break;
            }
            loca -= 1;

            long king_piece = 0x8000000000000000L >>> loca;
            if ((king_piece & my_pieces) != 0) {
                if (pinned_piece >= 0) {
                    break;
                }
                else {
                    pinned_piece = loca;
                }
            }
            else if ((king_piece & cannot_pin) != 0) {
                break;
            }
            else if ((king_piece & can_pin) != 0) {
                if (pinned_piece >= 0) {
                    pin_rays[pinned_piece] = 0xff00000000000000L >>> 8 * (pinned_piece / 8);
                }
                break;
            }
        }

        if (croissant > -1) {
            if (white) {
                if (kingLocation / 8 == 3) {
                    int leftEp = croissant + 23;
                    if (croissant != 0 && (pawn & 0x8000000000000000L >>> leftEp) != 0) {
                        cannot_pin = all_pieces ^ can_pin ^ (0xc000000000000000L >>> leftEp);
                        loca = kingLocation;
                        while (true) {
                            if (loca % 8 == 0) {
                                break;
                            }
                            loca -= 1;

                            long king_piece = 0x8000000000000000L >>> loca;
                            if ((king_piece & cannot_pin) != 0) {
                                break;
                            }
                            else if ((king_piece & can_pin) != 0) {
                                pin_rays[leftEp] &= ~(0x8000000000000000L >>> (croissant + 16));
                            }
                        }

                        loca = kingLocation;
                        while (true) {
                            if (loca % 8 == 7) {
                                break;
                            }
                            loca += 1;

                            long king_piece = 0x8000000000000000L >>> loca;
                            if ((king_piece & cannot_pin) != 0) {
                                break;
                            }
                            else if ((king_piece & can_pin) != 0) {
                                pin_rays[leftEp] &= ~(0x8000000000000000L >>> (croissant + 16));
                            }
                        }
                    }

                    int rightEp = croissant + 25;
                    if (croissant != 7 && (pawn & 0x8000000000000000L >>> rightEp) != 0) {
                        cannot_pin = all_pieces ^ can_pin ^ (0xc000000000000000L >>> (rightEp - 1));
                        loca = kingLocation;
                        while (true) {
                            if (loca % 8 == 0) {
                                break;
                            }
                            loca -= 1;

                            long king_piece = 0x8000000000000000L >>> loca;
                            if ((king_piece & cannot_pin) != 0) {
                                break;
                            }
                            else if ((king_piece & can_pin) != 0) {
                                pin_rays[rightEp] &= ~(0x8000000000000000L >>> (croissant + 16));
                            }
                        }

                        loca = kingLocation;
                        while (true) {
                            if (loca % 8 == 7) {
                                break;
                            }
                            loca += 1;

                            long king_piece = 0x8000000000000000L >>> loca;
                            if ((king_piece & cannot_pin) != 0) {
                                break;
                            }
                            else if ((king_piece & can_pin) != 0) {
                                pin_rays[rightEp] &= ~(0x8000000000000000L >>> (croissant + 16));
                            }
                        }
                    }
                }
            }
            else {
                if (kingLocation / 8 == 4) {
                    int leftEp = croissant + 31;
                    if (croissant != 0 && (pawn & 0x8000000000000000L >>> leftEp) != 0) {
                        cannot_pin = all_pieces ^ can_pin ^ (0xc000000000000000L >>> leftEp);
                        loca = kingLocation;
                        while (true) {
                            if (loca % 8 == 0) {
                                break;
                            }
                            loca -= 1;

                            long king_piece = 0x8000000000000000L >>> loca;
                            if ((king_piece & cannot_pin) != 0) {
                                break;
                            }
                            else if ((king_piece & can_pin) != 0) {
                                pin_rays[leftEp] &= ~(0x8000000000000000L >>> (croissant + 40));
                            }
                        }

                        loca = kingLocation;
                        while (true) {
                            if (loca % 8 == 7) {
                                break;
                            }
                            loca += 1;

                            long king_piece = 0x8000000000000000L >>> loca;
                            if ((king_piece & cannot_pin) != 0) {
                                break;
                            }
                            else if ((king_piece & can_pin) != 0) {
                                pin_rays[leftEp] &= ~(0x8000000000000000L >>> (croissant + 40));
                            }
                        }
                    }

                    int rightEp = croissant + 33;
                    if (croissant != 7 && (pawn & 0x8000000000000000L >>> rightEp) != 0) {
                        cannot_pin = all_pieces ^ can_pin ^ (0xc000000000000000L >>> (rightEp - 1));
                        loca = kingLocation;
                        while (true) {
                            if (loca % 8 == 0) {
                                break;
                            }
                            loca -= 1;

                            long king_piece = 0x8000000000000000L >>> loca;
                            if ((king_piece & cannot_pin) != 0) {
                                break;
                            }
                            else if ((king_piece & can_pin) != 0) {
                                pin_rays[rightEp] &= ~(0x8000000000000000L >>> (croissant + 40));
                            }
                        }

                        loca = kingLocation;
                        while (true) {
                            if (loca % 8 == 7) {
                                break;
                            }
                            loca += 1;

                            long king_piece = 0x8000000000000000L >>> loca;
                            if ((king_piece & cannot_pin) != 0) {
                                break;
                            }
                            else if ((king_piece & can_pin) != 0) {
                                pin_rays[rightEp] &= ~(0x8000000000000000L >>> (croissant + 40));
                            }
                        }
                    }
                }
            }
        }

        int cheqs = Long.bitCount(checks);

        if (checks == 0) {
            return noCheck(white, croissant, pin_rays, kingLocation, opp_pawn, opp_knight, opp_bishop, opp_rook, opp_queen, opp_king,
                    pawn, knight, bishop, rook, queen, king, opp_pieces, my_pieces, all_pieces, no_pieces, king_cannot_move, moves, index);
        }
        else if (cheqs == 1) {
            return singleCheck(white, croissant, pin_rays, checks, kingLocation, opp_pawn, opp_knight, opp_bishop, opp_rook, opp_queen,
                    pawn, knight, bishop, rook, queen, king, opp_pieces, my_pieces, all_pieces, no_pieces, king_cannot_move, moves, index);
        }
        else {
            return doubleCheck(white, king, my_pieces, king_cannot_move, moves, index, opp_pawn, opp_knight, opp_bishop, opp_rook, opp_queen);
        }
    }

    private int[] noCheck(boolean white, int croissant, long[] pin_rays, int kingLocation, long opp_pawn,
                            long opp_knight, long opp_bishop, long opp_rook, long opp_queen, long opp_king, long pawn, long knight,
                            long bishop, long rook, long queen, long king, long opp_pieces, long my_pieces, long all_pieces,
                            long no_pieces, long king_cannot_move, int[] moves, int index) {

        // Pawn Generation
        while (pawn != 0) {
            long piece = pawn & -pawn;
            int loc = Long.numberOfLeadingZeros(piece);
            long notPieceFileA = piece & notfileA;
            long notPieceFileH = piece & notfileH;
            long move = (white ? (notPieceFileA) << 9 : (notPieceFileH) >>> 9) & opp_pieces;
            move |= (white ? (notPieceFileH) << 7 : (notPieceFileA) >>> 7) & opp_pieces;
            move |= (white ? piece << 8 : piece >>> 8) & no_pieces;
            move |= (white ? (piece << 16) & 0xff000000L : (piece >>> 16) & 0xff00000000L) & no_pieces & (white ? no_pieces << 8 : no_pieces >>> 8);
            if (croissant > -1) {
                if (white) {
                    if (((loc == croissant + 25) && (loc % 8 != 0)) || ((loc == croissant + 23) && (loc % 8 != 7))) {
                        int thisMove = (croissant + 16);
                        moves[index] = loc << 26 | thisMove << 20 | (1) << 16 | 1 << 12;
                        index++;
                    }
                }
                else {
                    if (((loc == croissant + 33) && (loc % 8 != 0)) || ((loc == croissant + 31) && (loc % 8 != 7))) {
                        int thisMove = (croissant + 40);
                        moves[index] = loc << 26 | thisMove << 20 | 1 << 12;
                        index++;
                    }
                }
            }
            move &= pin_rays[loc];

            while (move != 0) {
                long thisMove = move & -move;
                int capture = 5;
                if ((thisMove & opp_pawn) != 0) {
                    capture = 0;
                }
                else if ((thisMove & opp_knight) != 0) {
                    capture = 1;
                }
                else if ((thisMove & opp_bishop) != 0) {
                    capture = 2;
                }
                else if ((thisMove & opp_rook) != 0) {
                    capture = 3;
                }
                else if ((thisMove & opp_queen) != 0) {
                    capture = 4;
                }
                if ((white && (thisMove & 0xff00000000000000L) != 0) || (!white && (thisMove & 0xffL) != 0)) {
                    for (int i = 1; i < 5; i++) {
                        moves[index] = loc << 26 | Long.numberOfLeadingZeros(thisMove) << 20
                                | (white ? 1 : 0) << 16 | i << 13 | capture << 9;
                        index++;
                    }
                    move ^= thisMove;
                }
                else {
                    moves[index] = loc << 26 | Long.numberOfLeadingZeros(thisMove) << 20 | (white ? 1 : 0) << 16 | capture << 9;
                    index++;
                    move ^= thisMove;
                }
            }

            pawn ^= piece;
        }

        // Knight Generation
        while (knight != 0) {
            long piece = knight & -knight;
            int loc = Long.numberOfLeadingZeros(piece);
            long notPieceFileA = piece & notfileA;
            long notPieceFileAB = piece & notfileAB;
            long notPieceFileH = piece & notfileH;
            long notPieceFileGH = piece & notfileGH;
            long move = notPieceFileA << 17;
            move |= notPieceFileH << 15;
            move |= notPieceFileAB << 10;
            move |= notPieceFileGH << 6;
            move |= notPieceFileAB >>> 6;
            move |= notPieceFileGH >>> 10;
            move |= notPieceFileA >>> 15;
            move |= notPieceFileH >>> 17;
            move &= ~my_pieces;
            move &= pin_rays[loc];

            while (move != 0) {
                long thisMove = move & -move;
                int capture = 5;
                if ((thisMove & opp_pawn) != 0) {
                    capture = 0;
                }
                else if ((thisMove & opp_knight) != 0) {
                    capture = 1;
                }
                else if ((thisMove & opp_bishop) != 0) {
                    capture = 2;
                }
                else if ((thisMove & opp_rook) != 0) {
                    capture = 3;
                }
                else if ((thisMove & opp_queen) != 0) {
                    capture = 4;
                }
                moves[index] = loc << 26 | Long.numberOfLeadingZeros(thisMove) << 20 | 0b1 << 17 | (white ? 1 : 0) << 16 | capture << 9;
                move ^= thisMove;
                index++;
            }

            knight ^= piece;
        }

        // Bishop Generation
        while (bishop != 0) {
            long piece = bishop & -bishop;
            int loc = Long.numberOfLeadingZeros(piece);

            long blockers = all_pieces & bishopMasks[loc];
            long move = bhm[loc][(int) ((magicBishops[loc] * blockers) >>> (64 - bishopMaskSize[loc]))];
            move &= ~my_pieces;
            move &= pin_rays[loc];

            while (move != 0) {
                long thisMove = move & -move;
                int capture = 5;
                if ((thisMove & opp_pawn) != 0) {
                    capture = 0;
                }
                else if ((thisMove & opp_knight) != 0) {
                    capture = 1;
                }
                else if ((thisMove & opp_bishop) != 0) {
                    capture = 2;
                }
                else if ((thisMove & opp_rook) != 0) {
                    capture = 3;
                }
                else if ((thisMove & opp_queen) != 0) {
                    capture = 4;
                }
                moves[index] = loc << 26 | Long.numberOfLeadingZeros(thisMove) << 20 | 0b10 << 17 | (white ? 1 : 0) << 16 | capture << 9;
                move ^= thisMove;
                index++;
            }

            bishop ^= piece;
        }

        // Rook Generation
        while (rook != 0) {
            long piece = rook & -rook;
            int loc = Long.numberOfLeadingZeros(piece);

            long blockers = all_pieces & rookMasks[loc];
            long move = rhm[loc][(int) ((magicRooks[loc] * blockers) >>> (64 - rookMaskSize[loc]))];
            move &= ~my_pieces;
            move &= pin_rays[loc];

            while (move != 0) {
                long thisMove = move & -move;
                int capture = 5;
                if ((thisMove & opp_pawn) != 0) {
                    capture = 0;
                }
                else if ((thisMove & opp_knight) != 0) {
                    capture = 1;
                }
                else if ((thisMove & opp_bishop) != 0) {
                    capture = 2;
                }
                else if ((thisMove & opp_rook) != 0) {
                    capture = 3;
                }
                else if ((thisMove & opp_queen) != 0) {
                    capture = 4;
                }
                moves[index] = loc << 26 | Long.numberOfLeadingZeros(thisMove) << 20 | 0b11 << 17 | (white ? 1 : 0) << 16 | capture << 9;
                move ^= thisMove;
                index++;
            }

            rook ^= piece;
        }

        // Queen Generation
        while (queen != 0) {
            long piece = queen & -queen;
            int loc = Long.numberOfLeadingZeros(piece);
            long blockers1 = all_pieces & ~piece & rookMasks[loc];
            long move1 = rhm[loc][(int) ((magicRooks[loc] * blockers1) >>> (64 - rookMaskSize[loc]))];

            long blockers2 = all_pieces & ~piece & bishopMasks[loc];
            long move2 = bhm[loc][(int) ((magicBishops[loc] * blockers2) >>> (64 - bishopMaskSize[loc]))];
            long move = move1 | move2;
            move &= ~my_pieces;
            move &= pin_rays[loc];

            while (move != 0) {
                long thisMove = move & -move;
                int capture = 5;
                if ((thisMove & opp_pawn) != 0) {
                    capture = 0;
                }
                else if ((thisMove & opp_knight) != 0) {
                    capture = 1;
                }
                else if ((thisMove & opp_bishop) != 0) {
                    capture = 2;
                }
                else if ((thisMove & opp_rook) != 0) {
                    capture = 3;
                }
                else if ((thisMove & opp_queen) != 0) {
                    capture = 4;
                }
                moves[index] = loc << 26 | Long.numberOfLeadingZeros(thisMove) << 20 | 0b100 << 17 | (white ? 1 : 0) << 16 | capture << 9;
                move ^= thisMove;
                index++;
            }

            queen ^= piece;
        }

        // King Generation
        while (king != 0) {
            long piece = king & -king;
            int loc = Long.numberOfLeadingZeros(piece);
            long notPieceFileA = piece & notfileA;
            long notPieceFileH = piece & notfileH;
            long move = (notPieceFileA) << 9;
            move |= piece << 8;
            move |= notPieceFileH << 7;
            move |= notPieceFileA << 1;
            move |= notPieceFileH >>> 1;
            move |= notPieceFileA >>> 7;
            move |= piece >>> 8;
            move |= notPieceFileH >>> 9;
            move &= ~my_pieces;
            move &= ~king_cannot_move;

            big:
            if ((states & (0x02 >>> (white ? 1 : 0))) != 0) {
                if (((states & (0x20 >>> (white ? 1 : 0))) == 0) && ((white ? 0x80L & white_rook : 0x8000000000000000L & black_rook)) != 0) {
                    if (white) {
                        if ((all_pieces & (0x70L)) == 0) {
                            int[] locss = new int[]{-1, -2};
                            for (int i : locss) {
                                int locaaaa = kingLocation + i;
                                long myPiece = 0x8000000000000000L >>> locaaaa;

                                long blockers = all_pieces & ~king & bishopMasks[locaaaa];
                                long moveeee = bhm[locaaaa][(int) ((magicBishops[locaaaa] * blockers) >>> (64 - bishopMaskSize[locaaaa]))];
                                moveeee &= (opp_bishop | opp_queen);

                                if (moveeee != 0) {
                                    break big;
                                }

                                blockers = all_pieces & ~king & rookMasks[locaaaa];
                                moveeee = rhm[locaaaa][(int) ((magicRooks[loc] * blockers) >>> (64 - rookMaskSize[locaaaa]))];
                                moveeee &= (opp_rook | opp_queen);

                                if (moveeee != 0) {
                                    break big;
                                }

                                long notPieceFileAAA = myPiece & notfileA;
                                long notPieceFileAB = myPiece & notfileAB;
                                long notPieceFileHHH = myPiece & notfileH;
                                long notPieceFileGH = myPiece & notfileGH;


                                moveeee = notPieceFileAAA << 9;
                                moveeee |= notPieceFileHHH << 7;
                                moveeee &= opp_pawn;

                                if (moveeee != 0) {
                                    break big;
                                }

                                moveeee = notPieceFileAAA << 17;
                                moveeee |= notPieceFileHHH << 15;
                                moveeee |= notPieceFileAB << 10;
                                moveeee |= notPieceFileGH << 6;
                                moveeee |= notPieceFileAB >>> 6;
                                moveeee |= notPieceFileGH >>> 10;
                                moveeee |= notPieceFileAAA >>> 15;
                                moveeee |= notPieceFileHHH >>> 17;
                                moveeee &= opp_knight;

                                if (moveeee != 0) {
                                    break big;
                                }

                                moveeee = (notPieceFileAAA) << 9;
                                moveeee |= myPiece << 8;
                                moveeee |= notPieceFileHHH << 7;
                                moveeee |= notPieceFileAAA << 1;
                                moveeee |= notPieceFileHHH >>> 1;
                                moveeee |= notPieceFileAAA >>> 7;
                                moveeee |= myPiece >>> 8;
                                moveeee |= notPieceFileHHH >>> 9;
                                moveeee &= opp_king;

                                if (moveeee != 0) {
                                    break big;
                                }
                            }
                            moves[index] = 0xf3ab0a00; // 0b111100111010
                            index++;
                        }
                    }
                    else {
                        if ((all_pieces & (0x7000000000000000L)) == 0) {
                            int[] locss = new int[]{-1, -2};
                            for (int i : locss) {
                                int locaaaa = kingLocation + i;
                                long myPiece = 0x8000000000000000L >>> locaaaa;

                                long blockers = all_pieces & ~king & bishopMasks[locaaaa];
                                long moveeee = bhm[locaaaa][(int) ((magicBishops[locaaaa] * blockers) >>> (64 - bishopMaskSize[locaaaa]))];
                                moveeee &= (opp_bishop | opp_queen);

                                if (moveeee != 0) {
                                    break big;
                                }

                                blockers = all_pieces & ~king & rookMasks[locaaaa];
                                moveeee = rhm[locaaaa][(int) ((magicRooks[loc] * blockers) >>> (64 - rookMaskSize[locaaaa]))];
                                moveeee &= (opp_rook | opp_queen);

                                if (moveeee != 0) {
                                    break big;
                                }

                                long notPieceFileAAA = myPiece & notfileA;
                                long notPieceFileAB = myPiece & notfileAB;
                                long notPieceFileHHH = myPiece & notfileH;
                                long notPieceFileGH = myPiece & notfileGH;

                                moveeee = notPieceFileAAA >>> 9;
                                moveeee |= notPieceFileHHH >>> 7;
                                moveeee &= opp_pawn;

                                if (moveeee != 0) {
                                    break big;
                                }

                                moveeee = notPieceFileAAA << 17;
                                moveeee |= notPieceFileHHH << 15;
                                moveeee |= notPieceFileAB << 10;
                                moveeee |= notPieceFileGH << 6;
                                moveeee |= notPieceFileAB >>> 6;
                                moveeee |= notPieceFileGH >>> 10;
                                moveeee |= notPieceFileAAA >>> 15;
                                moveeee |= notPieceFileHHH >>> 17;
                                moveeee &= opp_knight;

                                if (moveeee != 0) {
                                    break big;
                                }

                                moveeee = (notPieceFileAAA) << 9;
                                moveeee |= myPiece << 8;
                                moveeee |= notPieceFileHHH << 7;
                                moveeee |= notPieceFileAAA << 1;
                                moveeee |= notPieceFileHHH >>> 1;
                                moveeee |= notPieceFileAAA >>> 7;
                                moveeee |= myPiece >>> 8;
                                moveeee |= notPieceFileHHH >>> 9;
                                moveeee &= opp_king;

                                if (moveeee != 0) {
                                    break big;
                                }
                            }
                            moves[index] = 0x102a0a00;
                            index++;
                        }
                    }
                }
                if (((states & (0x4 >>> (white ? 1 : 0))) == 0) && ((white ? 0x1L & white_rook : 0x100000000000000L & black_rook)) != 0) {
                    if (white) {
                        if ((all_pieces & (0x6L)) == 0) {
                            int[] locss = new int[]{1, 2};
                            for (int i : locss) {
                                int locaaaa = kingLocation + i;
                                long myPiece = 0x8000000000000000L >>> locaaaa;

                                long blockers = all_pieces & ~king & bishopMasks[locaaaa];
                                long moveeee = bhm[locaaaa][(int) ((magicBishops[locaaaa] * blockers) >>> (64 - bishopMaskSize[locaaaa]))];
                                moveeee &= (opp_bishop | opp_queen);

                                if (moveeee != 0) {
                                    break big;
                                }

                                blockers = all_pieces & ~king & rookMasks[locaaaa];
                                moveeee = rhm[locaaaa][(int) ((magicRooks[loc] * blockers) >>> (64 - rookMaskSize[locaaaa]))];
                                moveeee &= (opp_rook | opp_queen);

                                if (moveeee != 0) {
                                    break big;
                                }

                                long notPieceFileAAA = myPiece & notfileA;
                                long notPieceFileAB = myPiece & notfileAB;
                                long notPieceFileHHH = myPiece & notfileH;
                                long notPieceFileGH = myPiece & notfileGH;


                                moveeee = notPieceFileAAA << 9;
                                moveeee |= notPieceFileHHH << 7;
                                moveeee &= opp_pawn;

                                if (moveeee != 0) {
                                    break big;
                                }

                                moveeee = notPieceFileAAA << 17;
                                moveeee |= notPieceFileHHH << 15;
                                moveeee |= notPieceFileAB << 10;
                                moveeee |= notPieceFileGH << 6;
                                moveeee |= notPieceFileAB >>> 6;
                                moveeee |= notPieceFileGH >>> 10;
                                moveeee |= notPieceFileAAA >>> 15;
                                moveeee |= notPieceFileHHH >>> 17;
                                moveeee &= opp_knight;

                                if (moveeee != 0) {
                                    break big;
                                }

                                moveeee = (notPieceFileAAA) << 9;
                                moveeee |= myPiece << 8;
                                moveeee |= notPieceFileHHH << 7;
                                moveeee |= notPieceFileAAA << 1;
                                moveeee |= notPieceFileHHH >>> 1;
                                moveeee |= notPieceFileAAA >>> 7;
                                moveeee |= myPiece >>> 8;
                                moveeee |= notPieceFileHHH >>> 9;
                                moveeee &= opp_king;

                                if (moveeee != 0) {
                                    break big;
                                }
                            }
                            moves[index] = 0xf3eb0a00;
                            index++;
                        }
                    }
                    else {
                        if ((all_pieces & (0x600000000000000L)) == 0) {
                            int[] locss = new int[]{1, 2};
                            for (int i : locss) {
                                int locaaaa = kingLocation + i;
                                long myPiece = 0x8000000000000000L >>> locaaaa;

                                long blockers = all_pieces & ~king & bishopMasks[locaaaa];
                                long moveeee = bhm[locaaaa][(int) ((magicBishops[locaaaa] * blockers) >>> (64 - bishopMaskSize[locaaaa]))];
                                moveeee &= (opp_bishop | opp_queen);

                                if (moveeee != 0) {
                                    break big;
                                }

                                blockers = all_pieces & ~king & rookMasks[locaaaa];
                                moveeee = rhm[locaaaa][(int) ((magicRooks[loc] * blockers) >>> (64 - rookMaskSize[locaaaa]))];
                                moveeee &= (opp_rook | opp_queen);

                                if (moveeee != 0) {
                                    break big;
                                }

                                long notPieceFileAAA = myPiece & notfileA;
                                long notPieceFileAB = myPiece & notfileAB;
                                long notPieceFileHHH = myPiece & notfileH;
                                long notPieceFileGH = myPiece & notfileGH;

                                moveeee = notPieceFileAAA >>> 9;
                                moveeee |= notPieceFileHHH >>> 7;
                                moveeee &= opp_pawn;

                                if (moveeee != 0) {
                                    break big;
                                }

                                moveeee = notPieceFileAAA << 17;
                                moveeee |= notPieceFileHHH << 15;
                                moveeee |= notPieceFileAB << 10;
                                moveeee |= notPieceFileGH << 6;
                                moveeee |= notPieceFileAB >>> 6;
                                moveeee |= notPieceFileGH >>> 10;
                                moveeee |= notPieceFileAAA >>> 15;
                                moveeee |= notPieceFileHHH >>> 17;
                                moveeee &= opp_knight;

                                if (moveeee != 0) {
                                    break big;
                                }

                                moveeee = (notPieceFileAAA) << 9;
                                moveeee |= myPiece << 8;
                                moveeee |= notPieceFileHHH << 7;
                                moveeee |= notPieceFileAAA << 1;
                                moveeee |= notPieceFileHHH >>> 1;
                                moveeee |= notPieceFileAAA >>> 7;
                                moveeee |= myPiece >>> 8;
                                moveeee |= notPieceFileHHH >>> 9;
                                moveeee &= opp_king;

                                if (moveeee != 0) {
                                    break big;
                                }
                            }
                            moves[index] = 0x106a0a00;
                            index++;
                        }
                    }
                }
            }

            while (move != 0) {
                long thisMove = move & -move;
                int capture = 5;
                if ((thisMove & opp_pawn) != 0) {
                    capture = 0;
                }
                else if ((thisMove & opp_knight) != 0) {
                    capture = 1;
                }
                else if ((thisMove & opp_bishop) != 0) {
                    capture = 2;
                }
                else if ((thisMove & opp_rook) != 0) {
                    capture = 3;
                }
                else if ((thisMove & opp_queen) != 0) {
                    capture = 4;
                }
                moves[index] = loc << 26 | Long.numberOfLeadingZeros(thisMove) << 20 | 0b101 << 17 | (white ? 1 : 0) << 16 | capture << 9;
                move ^= thisMove;
                index++;
            }

            king ^= piece;
        }

        if (index == 1) {
            moves[1] = 1;
            index++;
        }

        return Arrays.copyOfRange(moves, 1, index);
    }

    private int[] singleCheck(boolean white, int croissant, long[] pin_rays, long checks, int kingLocation, long opp_pawn,
                              long opp_knight, long opp_bishop, long opp_rook, long opp_queen, long pawn, long knight,
                              long bishop, long rook, long queen, long king, long opp_pieces, long my_pieces, long all_pieces,
                              long no_pieces, long king_cannot_move, int[] moves, int index) {
        long knight_pawn = opp_knight | opp_pawn;
        long attackRay = 0xffffffffffffffffL;

        if ((checks & knight_pawn) != 0) {
            // Pawn Generation
            while (pawn != 0) {
                long piece = pawn & -pawn;
                int loc = Long.numberOfLeadingZeros(piece);
                long notPieceFileA = piece & notfileA;
                long notPieceFileH = piece & notfileH;
                long move = (white ? (notPieceFileA) << 9 : (notPieceFileH) >>> 9) & opp_pieces;
                move |= (white ? (notPieceFileH) << 7 : (notPieceFileA) >>> 7) & opp_pieces;
                move |= (white ? piece << 8 : piece >>> 8) & no_pieces;
                move |= (white ? (piece << 16) & 0xff000000L : (piece >>> 16) & 0xff00000000L) & no_pieces & (white ? no_pieces << 8 : no_pieces >>> 8);
                move &= checks;
                if (croissant > -1) {
                    if (white) {
                        if (((loc == croissant + 25) && (loc % 8 != 0)) || ((loc == croissant + 23) && (loc % 8 != 7))) {
                            move |= 0x8000000000000000L >>> (croissant + 16);
                        }
                    }
                    else {
                        if (((loc == croissant + 33) && (loc % 8 != 0)) || ((loc == croissant + 31) && (loc % 8 != 7))) {
                            move |= 0x8000000000000000L >>> (croissant + 40);
                        }
                    }
                }
                move &= pin_rays[loc];

                while (move != 0) {
                    long thisMove = move & -move;
                    int capture = 5;
                    if ((thisMove & opp_pawn) != 0) {
                        capture = 0;
                    }
                    else if ((thisMove & opp_knight) != 0) {
                        capture = 1;
                    }
                    else if ((thisMove & opp_bishop) != 0) {
                        capture = 2;
                    }
                    else if ((thisMove & opp_rook) != 0) {
                        capture = 3;
                    }
                    else if ((thisMove & opp_queen) != 0) {
                        capture = 4;
                    }
                    moves[index] = loc << 26 | Long.numberOfLeadingZeros(thisMove) << 20 | (white ? 1 : 0) << 16 | capture << 9;
                    move ^= thisMove;
                    index++;
                }

                pawn ^= piece;
            }

            // Knight Generation
            while (knight != 0) {
                long piece = knight & -knight;
                int loc = Long.numberOfLeadingZeros(piece);
                long notPieceFileA = piece & notfileA;
                long notPieceFileAB = piece & notfileAB;
                long notPieceFileH = piece & notfileH;
                long notPieceFileGH = piece & notfileGH;
                long move = notPieceFileA << 17;
                move |= notPieceFileH << 15;
                move |= notPieceFileAB << 10;
                move |= notPieceFileGH << 6;
                move |= notPieceFileAB >>> 6;
                move |= notPieceFileGH >>> 10;
                move |= notPieceFileA >>> 15;
                move |= notPieceFileH >>> 17;
                move &= ~my_pieces;
                move &= checks;
                move &= pin_rays[loc];

                while (move != 0) {
                    long thisMove = move & -move;
                    int capture = 5;
                    if ((thisMove & opp_pawn) != 0) {
                        capture = 0;
                    }
                    else if ((thisMove & opp_knight) != 0) {
                        capture = 1;
                    }
                    else if ((thisMove & opp_bishop) != 0) {
                        capture = 2;
                    }
                    else if ((thisMove & opp_rook) != 0) {
                        capture = 3;
                    }
                    else if ((thisMove & opp_queen) != 0) {
                        capture = 4;
                    }
                    moves[index] = loc << 26 | Long.numberOfLeadingZeros(thisMove) << 20 | 0b1 << 17 | (white ? 1 : 0) << 16 | capture << 9;
                    move ^= thisMove;
                    index++;
                }

                knight ^= piece;
            }

            // Bishop Generation
            while (bishop != 0) {
                long piece = bishop & -bishop;
                int loc = Long.numberOfLeadingZeros(piece);

                long blockers = all_pieces & bishopMasks[loc];
                long move = bhm[loc][(int) ((magicBishops[loc] * blockers) >>> (64 - bishopMaskSize[loc]))];
                move &= ~my_pieces;
                move &= pin_rays[loc];
                move &= checks;

                while (move != 0) {
                    long thisMove = move & -move;
                    int capture = 5;
                    if ((thisMove & opp_pawn) != 0) {
                        capture = 0;
                    }
                    else if ((thisMove & opp_knight) != 0) {
                        capture = 1;
                    }
                    else if ((thisMove & opp_bishop) != 0) {
                        capture = 2;
                    }
                    else if ((thisMove & opp_rook) != 0) {
                        capture = 3;
                    }
                    else if ((thisMove & opp_queen) != 0) {
                        capture = 4;
                    }
                    moves[index] = loc << 26 | Long.numberOfLeadingZeros(thisMove) << 20 | 0b10 << 17 | (white ? 1 : 0) << 16 | capture << 9;
                    move ^= thisMove;
                    index++;
                }

                bishop ^= piece;
            }

            // Rook Generation
            while (rook != 0) {
                long piece = rook & -rook;
                int loc = Long.numberOfLeadingZeros(piece);

                long blockers = all_pieces & rookMasks[loc];
                long move = rhm[loc][(int) ((magicRooks[loc] * blockers) >>> (64 - rookMaskSize[loc]))];
                move &= ~my_pieces;
                move &= pin_rays[loc];
                move &= checks;

                while (move != 0) {
                    long thisMove = move & -move;
                    int capture = 5;
                    if ((thisMove & opp_pawn) != 0) {
                        capture = 0;
                    }
                    else if ((thisMove & opp_knight) != 0) {
                        capture = 1;
                    }
                    else if ((thisMove & opp_bishop) != 0) {
                        capture = 2;
                    }
                    else if ((thisMove & opp_rook) != 0) {
                        capture = 3;
                    }
                    else if ((thisMove & opp_queen) != 0) {
                        capture = 4;
                    }
                    moves[index] = loc << 26 | Long.numberOfLeadingZeros(thisMove) << 20 | 0b11 << 17 | (white ? 1 : 0) << 16 | capture << 9;
                    move ^= thisMove;
                    index++;
                }

                rook ^= piece;
            }

            // Queen Generation
            while (queen != 0) {
                long piece = queen & -queen;
                int loc = Long.numberOfLeadingZeros(piece);
                long blockers1 = all_pieces & ~piece & rookMasks[loc];
                long move1 = rhm[loc][(int) ((magicRooks[loc] * blockers1) >>> (64 - rookMaskSize[loc]))];

                long blockers2 = all_pieces & ~piece & bishopMasks[loc];
                long move2 = bhm[loc][(int) ((magicBishops[loc] * blockers2) >>> (64 - bishopMaskSize[loc]))];
                long move = move1 | move2;
                move &= ~my_pieces;
                move &= pin_rays[loc];
                move &= checks;

                while (move != 0) {
                    long thisMove = move & -move;
                    int capture = 5;
                    if ((thisMove & opp_pawn) != 0) {
                        capture = 0;
                    }
                    else if ((thisMove & opp_knight) != 0) {
                        capture = 1;
                    }
                    else if ((thisMove & opp_bishop) != 0) {
                        capture = 2;
                    }
                    else if ((thisMove & opp_rook) != 0) {
                        capture = 3;
                    }
                    else if ((thisMove & opp_queen) != 0) {
                        capture = 4;
                    }
                    moves[index] = loc << 26 | Long.numberOfLeadingZeros(thisMove) << 20 | 0b100 << 17 | (white ? 1 : 0) << 16 | capture << 9;
                    move ^= thisMove;
                    index++;
                }

                queen ^= piece;
            }

            // King Generation
            while (king != 0) {
                long piece = king & -king;
                int loc = Long.numberOfLeadingZeros(piece);
                long notPieceFileA = piece & notfileA;
                long notPieceFileH = piece & notfileH;
                long move = (notPieceFileA) << 9;
                move |= piece << 8;
                move |= notPieceFileH << 7;
                move |= notPieceFileA << 1;
                move |= notPieceFileH >>> 1;
                move |= notPieceFileA >>> 7;
                move |= piece >>> 8;
                move |= notPieceFileH >>> 9;
                move &= ~my_pieces;
                move &= ~king_cannot_move;

                while (move != 0) {
                    long thisMove = move & -move;
                    int capture = 5;
                    if ((thisMove & opp_pawn) != 0) {
                        capture = 0;
                    }
                    else if ((thisMove & opp_knight) != 0) {
                        capture = 1;
                    }
                    else if ((thisMove & opp_bishop) != 0) {
                        capture = 2;
                    }
                    else if ((thisMove & opp_rook) != 0) {
                        capture = 3;
                    }
                    else if ((thisMove & opp_queen) != 0) {
                        capture = 4;
                    }
                    moves[index] = loc << 26 | Long.numberOfLeadingZeros(thisMove) << 20 | 0b101 << 17 | (white ? 1 : 0) << 16 | capture << 9;
                    move ^= thisMove;
                    index++;
                }

                king ^= piece;
            }
        }
        else {
            int leading = Long.numberOfLeadingZeros(checks);
            int difference = kingLocation - leading;
            int rowDiff = kingLocation / 8 - leading / 8;
            if (difference > 0) {
                if (rowDiff > 0) {
                    if (difference % 9 == 0) {
                        long starting = 0x8040201008040201L;
                        starting <<= (8 - rowDiff) * 9;
                        starting >>>= leading;
                        attackRay = starting;
                    }
                    else if (difference % 8 == 0) {
                        long starting = 0x8080808080808080L;
                        starting <<= (8 - rowDiff) * 8;
                        starting >>>= leading;
                        attackRay = starting;
                    }
                    else if (difference % 7 == 0) {
                        long starting = 0x8102040810204080L;
                        starting <<= (8 - rowDiff) * 7;
                        starting >>>= leading;
                        starting &= ~king;
                        attackRay = starting;
                    }
                }
                else {
                    long starting = 0xff00000000000000L;
                    starting <<= (8 - difference);
                    starting >>>= (leading);
                    attackRay = starting;
                }
            }
            else {

                if (rowDiff < 0) {
                    if (difference % 9 == 0) {
                        long starting = 0x8040201008040201L;
                        starting >>>= (8 + rowDiff) * 9;
                        starting <<= (63 - leading);
                        attackRay = starting;
                    }
                    else if (difference % 8 == 0) {
                        long starting = 0x101010101010101L;
                        starting >>>= (8 + rowDiff) * 8;
                        starting <<= (63 - leading);
                        attackRay = starting;
                    }
                    else if (difference % 7 == 0) {
                        long starting = 0x102040810204081L;
                        starting >>>= (8 + rowDiff) * 7;
                        starting <<= (63 - leading);
                        starting &= ~king;
                        attackRay = starting;
                    }
                }
                else {
                    long starting = 0xffL;
                    starting >>>= (8 + difference);
                    starting <<= (63 - leading);
                    attackRay = starting;
                }
            }

            // Pawn Generation
            while (pawn != 0) {
                long piece = pawn & -pawn;
                int loc = Long.numberOfLeadingZeros(piece);
                long notPieceFileA = piece & notfileA;
                long notPieceFileH = piece & notfileH;
                long move = (white ? (notPieceFileA) << 9 : (notPieceFileH) >>> 9) & opp_pieces;
                move |= (white ? (notPieceFileH) << 7 : (notPieceFileA) >>> 7) & opp_pieces;
                move |= (white ? piece << 8 : piece >>> 8) & no_pieces;
                move |= (white ? (piece << 16) & 0xff000000L : (piece >>> 16) & 0xff00000000L) & no_pieces & (white ? no_pieces << 8 : no_pieces >>> 8);
                if (croissant > -1) {
                    if (white) {
                        if (((loc == croissant + 25) && (loc % 8 != 0)) || ((loc == croissant + 23) && (loc % 8 != 7))) {
                            int thisMove = (croissant + 16);
                            moves[index] = loc << 26 | thisMove << 20 | (1) << 16 | 1 << 12;
                            index++;
                        }
                    }
                    else {
                        if (((loc == croissant + 33) && (loc % 8 != 0)) || ((loc == croissant + 31) && (loc % 8 != 7))) {
                            int thisMove = (croissant + 40);
                            moves[index] = loc << 26 | thisMove << 20 | 1 << 12;
                            index++;
                        }
                    }
                }
                move &= pin_rays[loc];
                move &= attackRay;

                while (move != 0) {
                    long thisMove = move & -move;
                    int capture = 5;
                    if ((thisMove & opp_pawn) != 0) {
                        capture = 0;
                    }
                    else if ((thisMove & opp_knight) != 0) {
                        capture = 1;
                    }
                    else if ((thisMove & opp_bishop) != 0) {
                        capture = 2;
                    }
                    else if ((thisMove & opp_rook) != 0) {
                        capture = 3;
                    }
                    else if ((thisMove & opp_queen) != 0) {
                        capture = 4;
                    }
                    moves[index] = loc << 26 | Long.numberOfLeadingZeros(thisMove) << 20 | (white ? 1 : 0) << 16 | capture << 9;
                    move ^= thisMove;
                    index++;
                }

                pawn ^= piece;
            }

            // Knight Generation
            while (knight != 0) {
                long piece = knight & -knight;
                int loc = Long.numberOfLeadingZeros(piece);
                long notPieceFileA = piece & notfileA;
                long notPieceFileAB = piece & notfileAB;
                long notPieceFileH = piece & notfileH;
                long notPieceFileGH = piece & notfileGH;
                long move = notPieceFileA << 17;
                move |= notPieceFileH << 15;
                move |= notPieceFileAB << 10;
                move |= notPieceFileGH << 6;
                move |= notPieceFileAB >>> 6;
                move |= notPieceFileGH >>> 10;
                move |= notPieceFileA >>> 15;
                move |= notPieceFileH >>> 17;
                move &= ~my_pieces;
                move &= pin_rays[loc];
                move &= attackRay;

                while (move != 0) {
                    long thisMove = move & -move;
                    int capture = 5;
                    if ((thisMove & opp_pawn) != 0) {
                        capture = 0;
                    }
                    else if ((thisMove & opp_knight) != 0) {
                        capture = 1;
                    }
                    else if ((thisMove & opp_bishop) != 0) {
                        capture = 2;
                    }
                    else if ((thisMove & opp_rook) != 0) {
                        capture = 3;
                    }
                    else if ((thisMove & opp_queen) != 0) {
                        capture = 4;
                    }
                    moves[index] = loc << 26 | Long.numberOfLeadingZeros(thisMove) << 20 | 0b1 << 17 | (white ? 1 : 0) << 16 | capture << 9;
                    move ^= thisMove;
                    index++;
                }

                knight ^= piece;
            }

            // Bishop Generation
            while (bishop != 0) {
                long piece = bishop & -bishop;
                int loc = Long.numberOfLeadingZeros(piece);

                long blockers = all_pieces & bishopMasks[loc];
                long move = bhm[loc][(int) ((magicBishops[loc] * blockers) >>> (64 - bishopMaskSize[loc]))];
                move &= ~my_pieces;
                move &= pin_rays[loc];
                move &= attackRay;

                while (move != 0) {
                    long thisMove = move & -move;
                    int capture = 5;
                    if ((thisMove & opp_pawn) != 0) {
                        capture = 0;
                    }
                    else if ((thisMove & opp_knight) != 0) {
                        capture = 1;
                    }
                    else if ((thisMove & opp_bishop) != 0) {
                        capture = 2;
                    }
                    else if ((thisMove & opp_rook) != 0) {
                        capture = 3;
                    }
                    else if ((thisMove & opp_queen) != 0) {
                        capture = 4;
                    }
                    moves[index] = loc << 26 | Long.numberOfLeadingZeros(thisMove) << 20 | 0b10 << 17 | (white ? 1 : 0) << 16 | capture << 9;
                    move ^= thisMove;
                    index++;
                }

                bishop ^= piece;
            }

            // Rook Generation
            while (rook != 0) {
                long piece = rook & -rook;
                int loc = Long.numberOfLeadingZeros(piece);

                long blockers = all_pieces & rookMasks[loc];
                long move = rhm[loc][(int) ((magicRooks[loc] * blockers) >>> (64 - rookMaskSize[loc]))];
                move &= ~my_pieces;
                move &= pin_rays[loc];
                move &= attackRay;

                while (move != 0) {
                    long thisMove = move & -move;
                    int capture = 5;
                    if ((thisMove & opp_pawn) != 0) {
                        capture = 0;
                    }
                    else if ((thisMove & opp_knight) != 0) {
                        capture = 1;
                    }
                    else if ((thisMove & opp_bishop) != 0) {
                        capture = 2;
                    }
                    else if ((thisMove & opp_rook) != 0) {
                        capture = 3;
                    }
                    else if ((thisMove & opp_queen) != 0) {
                        capture = 4;
                    }
                    moves[index] = loc << 26 | Long.numberOfLeadingZeros(thisMove) << 20 | 0b11 << 17 | (white ? 1 : 0) << 16 | capture << 9;
                    move ^= thisMove;
                    index++;
                }

                rook ^= piece;
            }

            // Queen Generation
            while (queen != 0) {
                long piece = queen & -queen;
                int loc = Long.numberOfLeadingZeros(piece);
                long blockers1 = all_pieces & ~piece & rookMasks[loc];
                long move1 = rhm[loc][(int) ((magicRooks[loc] * blockers1) >>> (64 - rookMaskSize[loc]))];

                long blockers2 = all_pieces & ~piece & bishopMasks[loc];
                long move2 = bhm[loc][(int) ((magicBishops[loc] * blockers2) >>> (64 - bishopMaskSize[loc]))];
                long move = move1 | move2;
                move &= ~my_pieces;
                move &= pin_rays[loc];
                move &= attackRay;

                while (move != 0) {
                    long thisMove = move & -move;
                    int capture = 5;
                    if ((thisMove & opp_pawn) != 0) {
                        capture = 0;
                    }
                    else if ((thisMove & opp_knight) != 0) {
                        capture = 1;
                    }
                    else if ((thisMove & opp_bishop) != 0) {
                        capture = 2;
                    }
                    else if ((thisMove & opp_rook) != 0) {
                        capture = 3;
                    }
                    else if ((thisMove & opp_queen) != 0) {
                        capture = 4;
                    }
                    moves[index] = loc << 26 | Long.numberOfLeadingZeros(thisMove) << 20 | 0b100 << 17 | (white ? 1 : 0) << 16 | capture << 9;
                    move ^= thisMove;
                    index++;
                }

                queen ^= piece;
            }

            // King Generation
            while (king != 0) {
                long piece = king & -king;
                int loc = Long.numberOfLeadingZeros(piece);
                long notPieceFileA = piece & notfileA;
                long notPieceFileH = piece & notfileH;
                long move = (notPieceFileA) << 9;
                move |= piece << 8;
                move |= notPieceFileH << 7;
                move |= notPieceFileA << 1;
                move |= notPieceFileH >>> 1;
                move |= notPieceFileA >>> 7;
                move |= piece >>> 8;
                move |= notPieceFileH >>> 9;
                move &= ~my_pieces;
                move &= ~king_cannot_move;

                while (move != 0) {
                    long thisMove = move & -move;
                    int capture = 5;
                    if ((thisMove & opp_pawn) != 0) {
                        capture = 0;
                    }
                    else if ((thisMove & opp_knight) != 0) {
                        capture = 1;
                    }
                    else if ((thisMove & opp_bishop) != 0) {
                        capture = 2;
                    }
                    else if ((thisMove & opp_rook) != 0) {
                        capture = 3;
                    }
                    else if ((thisMove & opp_queen) != 0) {
                        capture = 4;
                    }
                    moves[index] = loc << 26 | Long.numberOfLeadingZeros(thisMove) << 20 | 0b101 << 17 | (white ? 1 : 0) << 16 | capture << 9;
                    move ^= thisMove;
                    index++;
                }

                king ^= piece;
            }
        }

        if (index == 1) {
            moves[1] = 2;
            index++;
        }

        return Arrays.copyOfRange(moves, 1, index);
    }

    private int[] doubleCheck(boolean white, long king, long my_pieces, long king_cannot_move, int[] moves, int index, long opp_pawn,
                              long opp_knight, long opp_bishop, long opp_rook, long opp_queen) {
        while (king != 0) {
            long piece = king & -king;
            int loc = Long.numberOfLeadingZeros(piece);
            long notPieceFileA = piece & notfileA;
            long notPieceFileH = piece & notfileH;
            long move = (notPieceFileA) << 9;
            move |= piece << 8;
            move |= notPieceFileH << 7;
            move |= notPieceFileA << 1;
            move |= notPieceFileH >>> 1;
            move |= notPieceFileA >>> 7;
            move |= piece >>> 8;
            move |= notPieceFileH >>> 9;
            move &= ~my_pieces;
            move &= ~king_cannot_move;

            while (move != 0) {
                long thisMove = move & -move;
                int capture = 5;
                if ((thisMove & opp_pawn) != 0) {
                    capture = 0;
                } else if ((thisMove & opp_knight) != 0) {
                    capture = 1;
                } else if ((thisMove & opp_bishop) != 0) {
                    capture = 2;
                } else if ((thisMove & opp_rook) != 0) {
                    capture = 3;
                } else if ((thisMove & opp_queen) != 0) {
                    capture = 4;
                }
                moves[index] = loc << 26 | Long.numberOfLeadingZeros(thisMove) << 20 | 0b101 << 17 | (white ? 1 : 0) << 16 | capture << 9;
                move ^= thisMove;
                index++;
            }

            king ^= piece;
        }

        if (index == 1) {
            moves[1] = 2;
            index++;
        }

        return Arrays.copyOfRange(moves, 1, index);
    }

    public int[] qGetMoves(boolean white) {
        // Quiescence Search Move Generator
        return new int[256];
    }


    /**
     *Prints board from white's perspective
     */
    public void printBoard() {
        for (int rank = 7; rank >= 0; rank--) {
            System.out.print((rank + 1) + " ");
            for (int file = 7; file >= 0; file--) {
                long mask = 1L << (rank * 8 + file);
                String piece = "..";

                if ((white_pawn & mask) != 0) piece = "wP";
                else if ((black_pawn & mask) != 0) piece = "bP";
                else if ((white_knight & mask) != 0) piece = "wN";
                else if ((black_knight & mask) != 0) piece = "bN";
                else if ((white_bishop & mask) != 0) piece = "wB";
                else if ((black_bishop & mask) != 0) piece = "bB";
                else if ((white_rook & mask) != 0) piece = "wR";
                else if ((black_rook & mask) != 0) piece = "bR";
                else if ((white_queen & mask) != 0) piece = "wQ";
                else if ((black_queen & mask) != 0) piece = "bQ";
                else if ((white_king & mask) != 0) piece = "wK";
                else if ((black_king & mask) != 0) piece = "bK";

                System.out.print(piece + " ");
            }
            System.out.println();
        }
        System.out.println("  A  B  C  D  E  F  G  H");
    }

    /**
     * Prints legal moves for a side
     *
     * @param white true/false. Determines whether white or black's moves are printed.
     */
    public void printMoves(boolean white, int lastMove) {
        // move format: int, from, to, type, side, en passant, promotion, capture = 23 bits;
        // type: 0 = pawn, 1 = knight, 2 = bishop, 3 = rook, 4 = queen, 5 = king
        // capture: 0 = pawn, 1 = knight, 2 = bishop, 3 = rook, 4 = queen, 5 = empty
        int croist = -1;
        if (lastMove != 100) {
            int from = lastMove >>> 26;
            int to = (lastMove << 6) >>> 26;
            int piece = (lastMove << 12) >>> 29;

            if (piece == 0) {
                if (to - from == 16 || from - to == 16) {
                    croist = to % 8;
                }
            }
        }

        int[] moves = nGetMoves(white, croist);

        String side = (white) ? "White:" : "Black:";
        System.out.println(side);

        for (int m : moves) {

            String piece = "";

            String from = "" + (char) ('A' + ((m >>> 26) % 8)) + (char) ('8' - ((m >>> 26) / 8));
            String to = "" + (char) ('A' + (((m << 6) >>> 26) % 8)) + (char) ('8' - (((m << 6) >>> 26) / 8));

            switch ((m << 12) >>> 29) {
                case 0: piece = "Pawn"; break;
                case 1: piece = "Knight"; break;
                case 2: piece = "Bishop"; break;
                case 3: piece = "Rook"; break;
                case 4: piece = "Queen"; break;
                case 5: piece = "King"; break;
            }

            System.out.println(" - " +piece + " on " + from + " to " + to);
        }
    }

    /**
     * Displays current board
     *
     * @param white true/false. Determines which side the board is viewed
     */
    public void showBoard(boolean white) {
        ChessFrame f = new ChessFrame();
        ChessPanel p = new ChessPanel(this, white);
        f.add(p);
    }
}

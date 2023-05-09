package npuzzlegame;

import java.util.*;

public class Solver {

  protected Stack<Board> closed = new Stack<>();
  private final Board INITIAL;

  public Solver(Board initial) {
    this.INITIAL = initial;
    this.INITIAL.g = 0;
    this.INITIAL.h = INITIAL.manhattan() + 2 * INITIAL.numLinearConflict();
    this.INITIAL.f = INITIAL.g + INITIAL.h;
  }

  public Stack<Board> AStar() {
    if (!closed.isEmpty()) {
      return closed;
    }

    Board x = INITIAL;
    closed.add(x);
    Queue<Board> opened = new PriorityQueue<>(Comparator.comparing(o->o.f));

    for (Board nextStep : x.nextSteps()) {
      // Tính giá trị hàm heuristic bằng khoảng cách manhattan + 2 lần số xung đột tuyến tính
      nextStep.prevBoard = x;
      nextStep.g = x.g + 1;
      nextStep.h = nextStep.manhattan() + 2 * nextStep.numLinearConflict();
      nextStep.f = nextStep.g + nextStep.h;
      //---------------------------//
      opened.add(nextStep);
    }

    while (!opened.isEmpty()) {
      // Kiểm tra xem bước tiếp theo đã đi qua chưa
      for (Board prevStep : closed) {
        if (prevStep.equals(opened.peek())) {
          opened.poll();
        }
      }

      if (x.isGoal()) {
        break;
      }

      x = opened.poll();
      closed.add(x);

      // Tính hàm heuristic và thêm vào tập mở
      for (Board nextStep : x.nextSteps()) {
        nextStep.prevBoard = x;
        nextStep.g += x.g;
        nextStep.h = nextStep.manhattan() + 2 * nextStep.numLinearConflict();
        nextStep.f = nextStep.g + nextStep.h;

        // Kiểm tra xem bước tiếp theo đã được tìm thấy chưa
        // Nếu đã tìm thấy trước đó và độ ưu tiên thấp hơn thì thay thế
        for (Board step : opened) {
          if (nextStep.equals(step) && nextStep.f < step.f) {
            opened.remove(step);
            break;
          }
        }

        opened.add(nextStep);
      }
    }

    closed.clear();

    // Truy vết lại đường đi tới đích
    while (x != null) {
      closed.add(x);
      x = x.prevBoard;
    }
    closed.pop();
    return closed;
  }

  public int minimumSteps() {
    AStar();
    return closed.size();
  }
}

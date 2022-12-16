package core.advanced.trace.threadlocal;

import core.advanced.trace.threadlocal.code.FieldService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static java.lang.Thread.sleep;

@Slf4j
public class FieldServiceTest {

    private FieldService fieldService = new FieldService();

    @Test
    void field() throws InterruptedException {
        log.info("main start");
        Runnable userA = () -> {
            fieldService.logic("userA");
        };

        Runnable userB = () -> {
            fieldService.logic("userB");
        };

        Thread threadA = new Thread(userA);
        threadA.setName("thread-A");
        Thread threadB = new Thread(userB);
        threadB.setName("thread-B");

        threadA.start();
        sleep(1500);
        // sleep(500) 시 출력 결과 *동시성 문제*
        // 13:33:17.285 [thread-A] INFO core.advanced.trace.threadlocal.code.FieldService - 저장 name = userA -> nameStore = null
        // 13:33:17.786 [thread-B] INFO core.advanced.trace.threadlocal.code.FieldService - 저장 name = userB -> nameStore = userA
        // 13:33:18.299 [thread-A] INFO core.advanced.trace.threadlocal.code.FieldService - 조회 nameStore = userB
        // 13:33:18.796 [thread-B] INFO core.advanced.trace.threadlocal.code.FieldService - 조회 nameStore = userB
        threadB.start();

        // 메서드 수행 시간 1초 인데 밑에 sleep 코드를 추가하지 않으면
        // 메서드가 끝나지 않고 메인 메서드를 종료 시키기 때문에 sleep(1500)을 추가했습니다.
        sleep(1500); // CountDownLatch 이용 가능
    }
}

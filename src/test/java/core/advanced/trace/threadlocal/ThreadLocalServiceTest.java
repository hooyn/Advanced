package core.advanced.trace.threadlocal;

import core.advanced.trace.threadlocal.code.FieldService;
import core.advanced.trace.threadlocal.code.ThreadLocalService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static java.lang.Thread.sleep;

@Slf4j
public class ThreadLocalServiceTest {

    private ThreadLocalService service = new ThreadLocalService();

    @Test
    void field() throws InterruptedException {
        log.info("main start");
        Runnable userA = () -> {
            service.logic("userA");
        };

        Runnable userB = () -> {
            service.logic("userB");
        };

        Thread threadA = new Thread(userA);
        threadA.setName("thread-A");
        Thread threadB = new Thread(userB);
        threadB.setName("thread-B");

        threadA.start();
        sleep(500);
        // sleep(500) 시 출력 결과 *동시성 문제*
        // 13:43:35.756 [thread-A] INFO core.advanced.trace.threadlocal.code.ThreadLocalService - 저장 name = userA -> nameStore = null
        // 13:43:36.270 [thread-B] INFO core.advanced.trace.threadlocal.code.ThreadLocalService - 저장 name = userB -> nameStore = null
        // 13:43:36.764 [thread-A] INFO core.advanced.trace.threadlocal.code.ThreadLocalService - 조회 nameStore = userA
        // 13:43:37.286 [thread-B] INFO core.advanced.trace.threadlocal.code.ThreadLocalService - 조회 nameStore = userB
        threadB.start();

        // 메서드 수행 시간 1초 인데 밑에 sleep 코드를 추가하지 않으면
        // 메서드가 끝나지 않고 메인 메서드를 종료 시키기 때문에 sleep(1500)을 추가했습니다.
        sleep(1500); // CountDownLatch 이용 가능
    }
}

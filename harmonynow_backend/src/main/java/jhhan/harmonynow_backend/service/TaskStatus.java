/**
 * TaskStatus
 *
 * [1. 역할]
 * - 음악 생성 작업의 현재 상태를 나타내는 열거형(enum) 클래스.
 * - 작업의 진행 상황과 결과 상태를 표현하는 데 사용.
 *
 * [2. 주요 기능]
 * - 작업 상태를 네 가지로 분류:
 *   1. IN_PROGRESS: 작업이 진행 중임을 나타냄.
 *   2. FAILED: 작업이 실패했음을 나타냄.
 *   3. COMPLETED_PENDING_RESULT: 작업이 완료되었으나 결과가 아직 제공되지 않음을 나타냄.
 *   4. COMPLETED_RESULT_PROVIDED: 작업이 완료되고 결과가 제공되었음을 나타냄.
 *
 * [3. 사용 사례]
 * - TaskService에서 작업 상태를 저장, 조회, 업데이트할 때 사용.
 * - 비동기 작업의 상태를 관리하는 데 활용.
 *
 */

package jhhan.harmonynow_backend.service;

public enum TaskStatus {
    IN_PROGRESS,
    FAILED,
    COMPLETED_PENDING_RESULT,
    COMPLETED_RESULT_PROVIDED;
}

package main

import (
    "fmt"
    "log"
    "os"
    "sync"
    "time"
)

type Task struct {
    ID   int
    Name string
}

func worker(id int, tasks <-chan Task, results chan<- string, wg *sync.WaitGroup) {
    defer wg.Done()
    log.Printf("Worker %d starting", id)

    for t := range tasks {
        log.Printf("Worker %d processing: %v", id, t)
        time.Sleep(time.Duration(100+id*50) * time.Millisecond)

        res := fmt.Sprintf("Worker %d processed: %s -> %d", id, t.Name, len(t.Name))

        select {
        case results <- res:
        default:
            log.Printf("Worker %d: result channel full, dropping result for %v", id, t)
        }
    }

    log.Printf("Worker %d finished", id)
}

func main() {
    log.SetFlags(log.LstdFlags | log.Lmicroseconds)

    numWorkers := 4
    tasks := make(chan Task)
    results := make(chan string, 100)

    var wg sync.WaitGroup
    wg.Add(numWorkers)

    // start workers
    for i := 1; i <= numWorkers; i++ {
        go worker(i, tasks, results, &wg)
    }

    // produce tasks
    go func() {
        for i := 1; i <= 20; i++ {
            tasks <- Task{ID: i, Name: fmt.Sprintf("task-%d", i)}
        }
        close(tasks)
    }()

    // collect results
    var resultList []string
    var mu sync.Mutex
    done := make(chan struct{})

    go func() {
        for r := range results {
            mu.Lock()
            resultList = append(resultList, r)
            mu.Unlock()
        }
        close(done)
    }()

    // wait for workers
    wg.Wait()
    close(results)
    <-done

    // write results to file
    f, err := os.Create("go-results.txt")
    if err != nil {
        log.Fatalf("Failed to create file: %v", err)
    }
    defer f.Close()

    mu.Lock()
    for _, r := range resultList {
        f.WriteString(r + "\n")
    }
    mu.Unlock()

    log.Printf("Results written to go-results.txt")
}
